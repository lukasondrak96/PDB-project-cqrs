package cz.vutbr.fit.pdb.projekt.commands.services;

import cz.vutbr.fit.pdb.projekt.commands.dto.group.NewGroupDto;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocumentRepository;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocumentRepository;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentGroup;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupTable;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserRepository;
import lombok.AllArgsConstructor;
import org.greenrobot.eventbus.EventBus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GroupCommandService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final UserDocumentRepository userDocumentRepository;


    private final GroupDocumentRepository groupDocumentRepository;
    private static final EventBus EVENT_BUS = EventBus.getDefault();

    public ResponseEntity<?> createGroup(NewGroupDto newGroupDto) {
//        if (groupRepository.countByName(newGroupDto.getName()) != 0) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Skupina s tímto názvem již existuje");
//        }
//
//        if (newGroupDto.getState() == GroupState.ARCHIVED) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nelze vytvořit archivovanou skupinu");
//        }
//
//        Optional<UserTable> userOptional = userRepository.findById(newGroupDto.getIdUser());
//        if (userOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uživatel s tímto id neexistuje");
//        }
//        UserTable author = userOptional.get();
//
//
//        Optional<UserDocument> userDocumentOptional = userDocumentRepository.findByEmail(author.getEmail());
//        if (userDocumentOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uživatel s tímto id neexistuje v mongo (v oracle existuje -> nekompatibilní stav databází)");
//        }
//
//        final GroupTable groupTable = new GroupTable(newGroupDto.getName(), newGroupDto.getDescription(), newGroupDto.getState(), author);
//
//        AuthorInherited groupAuthor = new AuthorInherited(author.getName(), author.getSurname());
//        final GroupDocument groupDocument = new GroupDocument(newGroupDto.getName(), newGroupDto.getDescription(), newGroupDto.getState().name(), groupAuthor, null, null);
//
//        new GroupEventSubscriber<>(groupTable, EVENT_BUS);
//        new GroupEventSubscriber<>(groupDocument, EVENT_BUS);
//
//        final GroupCreatedEvent createdEvent = new GroupCreatedEvent(this);
//        EVENT_BUS.post(createdEvent);

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
}
