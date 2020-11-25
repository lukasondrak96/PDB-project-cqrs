package cz.vutbr.fit.pdb.projekt.commands.services;

import cz.vutbr.fit.pdb.projekt.commands.dto.group.NewGroupDto;
import cz.vutbr.fit.pdb.projekt.events.events.group.GroupCreatedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.user.UserCreatedEvent;
import cz.vutbr.fit.pdb.projekt.events.subscribers.GroupEventSubscriber;
import cz.vutbr.fit.pdb.projekt.events.subscribers.UserEventSubscriber;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocumentRepository;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.inherited.AuthorInherited;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocumentRepository;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentGroup;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupRepository;
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
public class GroupCommandService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final UserDocumentRepository userDocumentRepository;


    private final GroupDocumentRepository groupDocumentRepository;
    private static final EventBus EVENT_BUS = EventBus.getDefault();

    public ResponseEntity<?> createGroup(NewGroupDto newGroupDto) {
        if (groupRepository.countByName(newGroupDto.getName()) != 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Skupina s tímto názvem již existuje");
        }

        Optional<UserTable> userOptional = userRepository.findById(newGroupDto.getIdUser());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uživatel s tímto id neexistuje");
        }
        UserTable author = userOptional.get();


        Optional<UserDocument> userDocumentOptional = userDocumentRepository.findByEmail(author.getEmail());
        if (userDocumentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uživatel s tímto id neexistuje v mongo (v oracle existuje -> nekompatibilní stav databází)");
        }

        final GroupTable groupTable = new GroupTable(newGroupDto.getName(), newGroupDto.getDescription(), newGroupDto.getState(), author);

        AuthorInherited groupAuthor = new AuthorInherited(author.getName(), author.getSurname());
        final GroupDocument groupDocument = new GroupDocument(newGroupDto.getName(), newGroupDto.getDescription(), newGroupDto.getState().name(), groupAuthor, null, null);

        new GroupEventSubscriber<>(groupTable, EVENT_BUS);
        new GroupEventSubscriber<>(groupDocument, EVENT_BUS);

        final GroupCreatedEvent createdEvent = new GroupCreatedEvent(this);
        EVENT_BUS.post(createdEvent);

        return ResponseEntity.ok().body("Skupina byla vytvořena");
    }

    public PersistentGroup updateGroup(PersistentGroup persistentGroup) {
        //todo
        return null;
    }

    public PersistentGroup saveGroup(PersistentGroup group) {
        if (group instanceof GroupTable)
            return groupRepository.save((GroupTable) group);
        else
            return groupDocumentRepository.save((GroupDocument) group);
    }

    public PersistentGroup deleteGroup(PersistentGroup persistentGroup) {
        return null;
    }
}
