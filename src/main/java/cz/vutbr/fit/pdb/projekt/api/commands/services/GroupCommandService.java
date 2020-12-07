package cz.vutbr.fit.pdb.projekt.api.commands.services;

import cz.vutbr.fit.pdb.projekt.api.commands.dtos.group.NewGroupDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.group.UpdateGroupDto;
import cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices.DeleteCommandService;
import cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices.GroupChangingService;
import cz.vutbr.fit.pdb.projekt.events.events.AbstractEvent;
import cz.vutbr.fit.pdb.projekt.events.events.OracleCreatedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.group.*;
import cz.vutbr.fit.pdb.projekt.events.subscribers.group.MongoGroupEventSubscriber;
import cz.vutbr.fit.pdb.projekt.events.subscribers.group.OracleGroupEventSubscriber;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.objects.GroupInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentGroup;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocumentRepository;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.CreatorEmbedded;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.MemberEmbedded;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.embedded.GroupEmbedded;
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

import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
@AllArgsConstructor
public class GroupCommandService implements GroupChangingService<PersistentGroup>, DeleteCommandService<PersistentGroup> {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupDocumentRepository groupDocumentRepository;
    private final MongoTemplate mongoTemplate;

    private static final EventBus EVENT_BUS = EventBus.getDefault();

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

    public ResponseEntity<?> updateGroup(int groupId, UpdateGroupDto updateGroupDto) {
        Optional<GroupTable> groupTableOptional = groupRepository.findById(groupId);
        if (groupTableOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tato skupina neexistuje");

        GroupTable oldGroupTable = groupTableOptional.get();

        if (isGroupTableEqualToUpdateGroupDto(oldGroupTable, updateGroupDto)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nebyly nalezeny žádné změny pro editaci");
        }

        final GroupTable groupTable = new GroupTable(groupId, updateGroupDto.getName(), updateGroupDto.getDescription(),
                oldGroupTable.getState(), oldGroupTable.getUserReference(), oldGroupTable.getUsers());

        GroupUpdatedEvent<PersistentGroup> updatedEvent = new GroupUpdatedEvent<>(groupTable, this);
        subscribeEventToOracleAndMongo(updatedEvent);

        return ResponseEntity.ok().body("Skupina byla aktualizována");
    }

    public ResponseEntity<?> deleteGroup(int groupId) {
        Optional<GroupTable> groupTableOptional = groupRepository.findById(groupId);
        if (groupTableOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Skupina s tímto id neexistuje");

        GroupTable groupTable = groupTableOptional.get();

        GroupDeletedEvent<PersistentGroup> deletedEvent = new GroupDeletedEvent<>(groupTable, this);
        subscribeEventToOracleAndMongo(deletedEvent);

        return ResponseEntity.ok().body("Skupina byla smazána");
    }

    public ResponseEntity<?> changeGroupState(int groupId, GroupState groupState) {
        Optional<GroupTable> groupTableOptional = groupRepository.findById(groupId);
        if (groupTableOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Skupina s tímto id neexistuje");

        GroupTable groupTable = groupTableOptional.get();
        if (groupTable.getState() == groupState)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Skupina již má stav " + groupState.name());

        GroupStateChangedEvent<PersistentGroup> groupStateChangedEvent = new GroupStateChangedEvent<>(groupTable, groupState, this);
        subscribeEventToOracleAndMongo(groupStateChangedEvent);

        return ResponseEntity.ok().body("Stav skupiny byl změněn");
    }


    public ResponseEntity<?> changeGroupAdmin(int groupId, int userId) {
        Optional<GroupTable> groupTableOptional = groupRepository.findById(groupId);
        if (groupTableOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Skupina s tímto id neexistuje");
        GroupTable groupTable = groupTableOptional.get();

        Optional<UserTable> userTableOptional = userRepository.findById(userId);
        if (userTableOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uživatel s tímto id neexistuje");
        UserTable userTable = userTableOptional.get();

        GroupAdminChangedEvent<PersistentGroup> groupAdminChangedEvent = new GroupAdminChangedEvent<>(groupTable, userTable, this);
        subscribeEventToOracleAndMongo(groupAdminChangedEvent);

        return ResponseEntity.ok().body("Admin skupiny byl změněn");
    }


    public ResponseEntity<?> addGroupMember(int groupId, int userId) {
        Optional<GroupTable> groupTableOptional = groupRepository.findById(groupId);
        if (groupTableOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Skupina s tímto id neexistuje");
        GroupTable groupTable = groupTableOptional.get();

        Optional<UserTable> userTableOptional = userRepository.findById(userId);
        if (userTableOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uživatel s tímto id neexistuje");
        UserTable userTable = userTableOptional.get();

        groupTable.addUser(userTable);

        GroupMemberAddedEvent<PersistentGroup> groupMemberAddedEvent = new GroupMemberAddedEvent<>(groupTable, userTable, this);
        subscribeEventToOracleAndMongo(groupMemberAddedEvent);

        return ResponseEntity.ok().body("Uživatel byl přidán do skupiny");
    }

    public ResponseEntity<?> removeGroupMember(int groupId, int userId) {
        Optional<GroupTable> groupTableOptional = groupRepository.findById(groupId);
        if (groupTableOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Skupina s tímto id neexistuje");
        GroupTable groupTable = groupTableOptional.get();

        Optional<UserTable> userTableOptional = userRepository.findById(userId);
        if (userTableOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uživatel s tímto id neexistuje");
        UserTable userTable = userTableOptional.get();

        GroupMemberRemovedEvent<PersistentGroup> groupMemberRemovedEvent = new GroupMemberRemovedEvent<>(groupTable, userTable, this);
        subscribeEventToOracleAndMongo(groupMemberRemovedEvent);

        return ResponseEntity.ok().body("Uživatel byl odebrán ze skupiny");
    }

    /* methods called from events */
    @Override
    public PersistentGroup assignFromTo(ObjectInterface objectInterface, PersistentGroup group) {
        GroupInterface persistentGroupInterface = (GroupInterface) group;
        GroupInterface groupInterface = (GroupInterface) objectInterface;
        if (group instanceof GroupTable || group instanceof GroupDocument) {
            persistentGroupInterface.setId(groupInterface.getId());
            persistentGroupInterface.setName(groupInterface.getName());
            persistentGroupInterface.setState(groupInterface.getState());
            persistentGroupInterface.setDescription(groupInterface.getDescription());
            persistentGroupInterface.setUserReference(groupInterface.getUserReference());
        }

        if (group instanceof GroupTable) {
            ((GroupTable) persistentGroupInterface).setUsers(((GroupTable) groupInterface).getUsers());
        } else if (group instanceof GroupDocument) {
            ArrayList<MemberEmbedded> memberEmbeddings = new ArrayList<>();
            ((GroupTable) groupInterface).getUsers().forEach(user -> {
                memberEmbeddings.add(new MemberEmbedded(user.getId(), user.getName(), user.getSurname()));
            });
            ((GroupDocument) persistentGroupInterface).setMembers(memberEmbeddings);
        }
        return (PersistentGroup) persistentGroupInterface;
    }

    @Override
    public PersistentGroup finishSaving(PersistentGroup group) {
        if (group instanceof GroupTable)
            return groupRepository.save((GroupTable) group);
        else {
            GroupDocument groupDocument = (GroupDocument) group;
            int idCreator = groupDocument.getCreator().getId();
            updateCreatorsUserDocument(idCreator, groupDocument);
            return groupDocumentRepository.save(groupDocument);
        }
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
            removeGroupInUsersGroupsMember(groupDocument);
            removeGroupInUsersGroupsAdmin(groupDocument);
            groupDocumentRepository.delete(groupDocument);
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

    @Override
    public PersistentGroup finishAdminChanging(PersistentGroup group, UserTable userTable) {
        if (group instanceof GroupTable) {
            GroupTable groupTable = (GroupTable) group;
            groupTable.setUserReference(userTable);
            return groupRepository.save(groupTable);
        } else {
            GroupDocument groupDocument = (GroupDocument) group;
            updateCreatorsUserDocument(userTable.getId(), groupDocument);
            groupDocument.setCreator(new CreatorEmbedded(userTable.getId(), userTable.getName(), userTable.getSurname()));
            return groupDocumentRepository.save(groupDocument);
        }
    }

    @Override
    public PersistentGroup finishMemberAdding(PersistentGroup group, UserTable userTable) {
        if (group instanceof GroupTable) {
            GroupTable groupTable = (GroupTable) group;
            return groupRepository.save(groupTable);
        } else {
            GroupDocument groupDocument = (GroupDocument) group;
            updateAddGroupMembersInUserDocument(userTable.getId(), groupDocument);
            return groupDocumentRepository.save(groupDocument);
        }
    }

    @Override
    public PersistentGroup finishMemberRemoving(PersistentGroup group, UserTable userTable) {
        if (group instanceof GroupTable) {
            GroupTable groupTable = (GroupTable) group;
            groupTable.removeUser(userTable);
            return groupRepository.save(groupTable);
        } else {
            GroupDocument groupDocument = (GroupDocument) group;
            updateRemoveGroupMembersInUserDocument(userTable.getId(), groupDocument);
            groupDocument.getMembers().remove(new MemberEmbedded(userTable.getId(), userTable.getName(), userTable.getSurname()));
            return groupDocumentRepository.save(groupDocument);
        }
    }

    /* private methods */
    private boolean isGroupTableEqualToUpdateGroupDto(GroupTable table, UpdateGroupDto dto) {
        return dto.getName().equals(table.getName()) &&
                dto.getDescription().equals(table.getDescription());
    }

    private void subscribeEventToOracleAndMongo(AbstractEvent<PersistentGroup> event) {
        OracleGroupEventSubscriber sqlSubscriber = new OracleGroupEventSubscriber(EVENT_BUS);
        MongoGroupEventSubscriber noSqlSubscriber = new MongoGroupEventSubscriber(EVENT_BUS);
        EVENT_BUS.post(event);

        EVENT_BUS.unregister(sqlSubscriber);
        EVENT_BUS.unregister(noSqlSubscriber);
    }


    private void updateAddGroupMembersInUserDocument(int newMemberId, GroupDocument groupDocument) {
        mongoTemplate.updateMulti(
                new Query(where("id").is(newMemberId)),
                new Update()
                        .push("groupsMember", new GroupEmbedded(groupDocument.getId(), groupDocument.getName())),
                UserDocument.class
        );
    }

    private void updateRemoveGroupMembersInUserDocument(int newMemberId, GroupDocument groupDocument) {
        mongoTemplate.updateMulti(
                new Query(where("id").is(newMemberId)),
                new Update()
                        .pull("groupsMember", new GroupEmbedded(groupDocument.getId(), groupDocument.getName())),
                UserDocument.class
        );
    }

    private void updateCreatorsUserDocument(int newCreatorId, GroupDocument groupDocument) {
        //remove group of group_admin of old creator (in case of create group this update does not needed)
        mongoTemplate.updateMulti(
                new Query(where("id").is(groupDocument.getCreator().getId())),
                new Update()
                        .pull("groupsAdmin", new GroupEmbedded(groupDocument.getId(), groupDocument.getName())),
                UserDocument.class
        );

        //add group to group_admin for new creator
        mongoTemplate.updateMulti(
                new Query(where("id").is(newCreatorId)),
                new Update()
                        .push("groupsAdmin", new GroupEmbedded(groupDocument.getId(), groupDocument.getName())),
                UserDocument.class
        );
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
