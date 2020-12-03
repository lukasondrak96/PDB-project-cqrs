package cz.vutbr.fit.pdb.projekt.api.commands.services;

import cz.vutbr.fit.pdb.projekt.api.commands.dtos.group.NewGroupDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.group.UpdateGroupDto;
import cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices.CommandDeleteService;
import cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices.GroupWithStateChangingService;
import cz.vutbr.fit.pdb.projekt.events.events.AbstractEvent;
import cz.vutbr.fit.pdb.projekt.events.events.OracleCreatedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.group.GroupDeletedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.group.GroupStateChangedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.group.GroupUpdatedEvent;
import cz.vutbr.fit.pdb.projekt.events.subscribers.group.MongoGroupEventSubscriber;
import cz.vutbr.fit.pdb.projekt.events.subscribers.group.OracleGroupEventSubscriber;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.objects.GroupInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentGroup;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocumentRepository;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupState;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupTable;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import lombok.AllArgsConstructor;
import org.greenrobot.eventbus.EventBus;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
@AllArgsConstructor
public class GroupCommandService implements GroupWithStateChangingService<PersistentGroup>, CommandDeleteService<PersistentGroup> {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;


    private final GroupDocumentRepository groupDocumentRepository;
    private static final EventBus EVENT_BUS = EventBus.getDefault();
    private final MongoTemplate mongoTemplate;

    public ResponseEntity<?> createGroup(NewGroupDto newGroupDto) {
        if (groupRepository.countByName(newGroupDto.getName()) != 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Skupina s tímto názvem již existuje");
        }

        if (newGroupDto.getState() == GroupState.ARCHIVED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nelze vytvořit archivovanou skupinu");
        }

        Optional<UserTable> userOptional = userRepository.findById(newGroupDto.getCreatorId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uživatel s tímto id neexistuje");
        }
        UserTable creator = userOptional.get();

        final GroupTable groupTable = new GroupTable(newGroupDto.getName(), newGroupDto.getDescription(), newGroupDto.getState(), creator);
        OracleCreatedEvent<PersistentGroup> oracleCreatedEvent = new OracleCreatedEvent<>(groupTable, this);
        subscribeEventToOracleAndMongo(oracleCreatedEvent);
        return ResponseEntity.ok().body("Skupina byla vytvořena");
    }

    public ResponseEntity<?> updateGroup(Integer groupId, UpdateGroupDto updateGroupDto) {
        Optional<GroupTable> groupTableOptional = groupRepository.findById(groupId);
        if(groupTableOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tato skupina neexistuje");

        GroupTable oldGroupTable = groupTableOptional.get();

        if (groupTableEqualsUpdateGroupDto(oldGroupTable, updateGroupDto)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nebyl nalezen záznam pro editaci");
        }

        final GroupTable groupTable = new GroupTable(groupId, updateGroupDto.getName(),
                updateGroupDto.getDescription(),
                oldGroupTable.getState(),
                oldGroupTable.getUserReference()
        );

        GroupUpdatedEvent<PersistentGroup> updatedEvent = new GroupUpdatedEvent<>(groupTable, this);
        subscribeEventToOracleAndMongo(updatedEvent);

        return ResponseEntity.ok().body("Data byla aktualizována");
    }

    public ResponseEntity<?> deleteGroup(int groupId) {
        Optional<GroupTable> groupTableOptional = groupRepository.findById(groupId);
        if (groupTableOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Skupina s tímto id neexistuje");

        GroupTable groupTable = groupTableOptional.get();

        GroupDeletedEvent<PersistentGroup> deletedEvent = new GroupDeletedEvent<>(groupTable,this);
        subscribeEventToOracleAndMongo(deletedEvent);

        return ResponseEntity.ok().body("Skupina byla smazána");
    }

    public ResponseEntity<?> changeGroupState(int groupId, GroupState groupState) {
        Optional<GroupTable> groupTableOptional = groupRepository.findById(groupId);
        if (groupTableOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Skupina s tímto id neexistuje");

        GroupTable groupTable = groupTableOptional.get();
        if(groupTable.getState() == groupState)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Skupina již má stav " + groupState.name());

        GroupStateChangedEvent<PersistentGroup> groupGroupStateChangedEvent = new GroupStateChangedEvent<>(groupTable, groupState, this);
        subscribeEventToOracleAndMongo(groupGroupStateChangedEvent);

        return ResponseEntity.ok().body("Stav skupiny byl změněn");
    }

/* methods called from events */
    @Override
    public PersistentGroup assignFromTo(ObjectInterface objectInterface, PersistentGroup group) {
        GroupInterface persistentGroupInterface = (GroupInterface) group;
        GroupInterface groupInterface = (GroupInterface) objectInterface;
        if(group instanceof GroupTable || group instanceof GroupDocument) {
            persistentGroupInterface.setId(groupInterface.getId());
            persistentGroupInterface.setName(groupInterface.getName());
            persistentGroupInterface.setState(groupInterface.getState());
            persistentGroupInterface.setDescription(groupInterface.getDescription());
            persistentGroupInterface.setUserReference(groupInterface.getUserReference());
        }
        return (PersistentGroup) persistentGroupInterface;
    }

    @Override
    public PersistentGroup finishSaving(PersistentGroup group) {
        if (group instanceof GroupTable)
            return groupRepository.save((GroupTable) group);
        else
            return groupDocumentRepository.save((GroupDocument) group);
    }

    @Override
    public PersistentGroup finishUpdating(PersistentGroup group) {
        if (group instanceof GroupTable) {
            return groupRepository.save((GroupTable) group);
        } else {
            GroupDocument groupDocument = (GroupDocument) group;
            updateNameOfGroupInUsersGroupsMember(groupDocument);
            updateNameOfGroupInUsersGroupsAdmin(groupDocument);
            return groupDocumentRepository.save(groupDocument);
        }
    }

    @Override
    public PersistentGroup finishDeleting(PersistentGroup group) {
        if (group instanceof GroupTable) {
            groupRepository.delete((GroupTable) group);
        } else {
            GroupDocument groupDocument = (GroupDocument) group;
            updateNameOfGroupInUsersGroupsMember(groupDocument);
            updateNameOfGroupInUsersGroupsAdmin(groupDocument);
            groupDocumentRepository.delete((GroupDocument) group);
        }
        return null;
    }

    @Override
    public PersistentGroup finishStateChanging(PersistentGroup group, GroupState state) {
        if (group instanceof GroupTable) {
            GroupTable groupTable = (GroupTable) group;
            groupTable.setState(state);
            return groupRepository.save(groupTable);
        } else {
            GroupDocument groupDocument = (GroupDocument) group;
            groupDocument.setState(state);
            return groupDocumentRepository.save(groupDocument);
        }
    }

/* private methods */
    private boolean groupTableEqualsUpdateGroupDto(GroupTable table, UpdateGroupDto dto) {
        return dto.getName().equals(table.getName()) &&
                dto.getDescription() == table.getDescription();
    }


    private void subscribeEventToOracleAndMongo(AbstractEvent<PersistentGroup> event) {
        OracleGroupEventSubscriber sqlSubscriber = new OracleGroupEventSubscriber(EVENT_BUS);
        MongoGroupEventSubscriber noSqlSubscriber = new MongoGroupEventSubscriber(EVENT_BUS);
        EVENT_BUS.post(event);

        EVENT_BUS.unregister(sqlSubscriber);
        EVENT_BUS.unregister(noSqlSubscriber);
    }

    private void updateNameOfGroupInUsersGroupsAdmin(GroupDocument groupDocument) {
        mongoTemplate.updateMulti(
                new Query(where("groups_member.id").is(groupDocument.getId())),
                new Update()
                        .set("groups_member.$.name", groupDocument.getName()),
                UserDocument.class
        );
    }

    private void updateNameOfGroupInUsersGroupsMember(GroupDocument groupDocument) {
        mongoTemplate.updateMulti(
                new Query(where("groups_admin.id").is(groupDocument.getId())),
                new Update()
                        .set("groups_admin.$.name", groupDocument.getName()),
                UserDocument.class
        );
    }

    private void removeGroupInUsersGroupsAdmin(GroupDocument groupDocument) {
        mongoTemplate.updateMulti(
                new Query(),
                new Update()
                        .pull("groups_member", Query.query(Criteria.where("groups_member.$id").is(groupDocument.getId()))),
                UserDocument.class
        );
    }

    private void removeGroupInUsersGroupsMember(GroupDocument groupDocument) {
        mongoTemplate.updateMulti(
                new Query(where("groups_admin.id").is(groupDocument.getId())),
                new Update()
                    .pull("groups_admin", Query.query(Criteria.where("groups_admin.$id").is(groupDocument.getId()))),
                UserDocument.class
        );
    }
}
