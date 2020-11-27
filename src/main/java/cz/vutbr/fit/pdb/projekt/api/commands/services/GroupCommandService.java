package cz.vutbr.fit.pdb.projekt.api.commands.services;

import cz.vutbr.fit.pdb.projekt.api.commands.dtos.group.NewGroupDto;
import cz.vutbr.fit.pdb.projekt.events.events.OracleCreatedEvent;
import cz.vutbr.fit.pdb.projekt.events.subscribers.group.MongoGroupEventSubscriber;
import cz.vutbr.fit.pdb.projekt.events.subscribers.group.OracleGroupEventSubscriber;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.objects.GroupInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentGroup;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocumentRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupState;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupTable;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import lombok.AllArgsConstructor;
import org.greenrobot.eventbus.EventBus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GroupCommandService implements CommandService<PersistentGroup> {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;


    private final GroupDocumentRepository groupDocumentRepository;
    private static final EventBus EVENT_BUS = EventBus.getDefault();

    public ResponseEntity<?> createGroup(NewGroupDto newGroupDto) {
        if (groupRepository.countByName(newGroupDto.getName()) != 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Skupina s tímto názvem již existuje");
        }

        if (newGroupDto.getState() == GroupState.ARCHIVED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nelze vytvořit archivovanou skupinu");
        }

        Optional<UserTable> userOptional = userRepository.findById(newGroupDto.getAuthorId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uživatel s tímto id neexistuje");
        }
        UserTable author = userOptional.get();

        final GroupTable groupTable = new GroupTable(newGroupDto.getName(), newGroupDto.getDescription(), newGroupDto.getState(), author);

        OracleGroupEventSubscriber oracleSubscriber = new OracleGroupEventSubscriber(EVENT_BUS);
        MongoGroupEventSubscriber mongoSubscriber = new MongoGroupEventSubscriber(EVENT_BUS);

        EVENT_BUS.post(new OracleCreatedEvent<>(groupTable, this));
        EVENT_BUS.unregister(oracleSubscriber);
        EVENT_BUS.unregister(mongoSubscriber);

        return ResponseEntity.ok().body("Skupina byla vytvořena");
    }

    public PersistentGroup updateGroup(PersistentGroup persistentGroup) {
        //todo
        return null;
    }

    public ResponseEntity<?> deleteGroup(String groupId) {
//        Optional<GroupDocument> groupDocumentOptional = groupDocumentRepository.findById(groupId);
//        if (groupDocumentOptional.isEmpty())
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Skupina s tímto id neexistuje");
//
//        GroupDocument groupDocument = groupDocumentOptional.get();
//
//        Optional<GroupTable> groupTableOptional = groupRepository.findByName(groupDocument.getName());
//        if (groupTableOptional.isEmpty())
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Group s tímto id neexistuje v oracle (v mongo existuje -> nekompatibilní stav databází)");
//        GroupTable groupTable = groupTableOptional.get();
//
//        new GroupEventSubscriber<>(groupTable, EVENT_BUS);
//        new GroupEventSubscriber<>(groupDocument, EVENT_BUS);
//
//        final GroupDeletedEvent deletedEvent = new GroupDeletedEvent(this);
//        EVENT_BUS.post(deletedEvent);

        return ResponseEntity.ok().body("Skupina byla smazána");
    }

/* methods called from events */
    public PersistentGroup finishGroupSaving(PersistentGroup group) {
        if (group instanceof GroupTable)
            return groupRepository.save((GroupTable) group);
        else
            return groupDocumentRepository.save((GroupDocument) group);
    }

    public void finishGroupDeleting(PersistentGroup group) {
        if (group instanceof GroupTable)
            groupRepository.delete((GroupTable) group);
        else
            groupDocumentRepository.delete((GroupDocument) group);
    }

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
    public PersistentGroup save(PersistentGroup group) {
        if (group instanceof GroupTable)
            return groupRepository.save((GroupTable) group);
        else
            return groupDocumentRepository.save((GroupDocument) group);
    }
}
