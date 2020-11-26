package cz.vutbr.fit.pdb.projekt.commands.services;

import cz.vutbr.fit.pdb.projekt.commands.dto.user.NewUserDto;
import cz.vutbr.fit.pdb.projekt.commands.dto.user.UpdateUserDto;
import cz.vutbr.fit.pdb.projekt.events.events.user.UserCreatedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.user.UserUpdatedEvent;
import cz.vutbr.fit.pdb.projekt.events.subscribers.UserEventSubscriber;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocumentRepository;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentUser;
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
public class UserCommandService {

    private final UserRepository userRepository;
    private final UserDocumentRepository userDocumentRepository;
    private static final EventBus EVENT_BUS = EventBus.getDefault();

    public ResponseEntity<?> createUser(NewUserDto newUserDto) {
        if (userRepository.countByEmail(newUserDto.getEmail()) != 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tento uživatel již existuje");
        }

        final UserTable userTable = new UserTable(newUserDto.getEmail(), newUserDto.getName(), newUserDto.getSurname(), newUserDto.getBirthDate(), newUserDto.getSex());
        final UserDocument userDocument = new UserDocument(newUserDto.getEmail(), newUserDto.getName(), newUserDto.getSurname(), newUserDto.getBirthDate(), newUserDto.getSex(), null, null, null);

        new UserEventSubscriber<>(userTable, EVENT_BUS);
        new UserEventSubscriber<>(userDocument, EVENT_BUS);

        final UserCreatedEvent createdEvent = new UserCreatedEvent(this);
        EVENT_BUS.post(createdEvent);

        return ResponseEntity.ok().body("Účet byl vytvořen");
    }

    public ResponseEntity<?> updateUser(String userId, UpdateUserDto updateUserDto) {
        Optional<UserDocument> userDocumentOptional = userDocumentRepository.findById(userId);
        if (userDocumentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tento uživatel neexistuje");
        }

        UserDocument oldUserDocument = userDocumentOptional.get();
        String email = oldUserDocument.getEmail();

        Optional<UserTable> userTableOptional = userRepository.findByEmail(email);
        if(userTableOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User s tímto emailem neexistuje v oracle (v mongo existuje -> nekompatibilní stav databází)");

        UserTable oldUserTable = userTableOptional.get();


        if (userTableEqualsUpdateUserDto(oldUserTable, updateUserDto)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nebyl nalezen záznam pro editaci");
        }

        final UserTable userTable = new UserTable(updateUserDto.getEmail(), updateUserDto.getName(),
                updateUserDto.getSurname(), updateUserDto.getBirthDate(), updateUserDto.getSex(), updateUserDto.getState());
        userTable.setIdUser(oldUserTable.getIdUser());

        final UserDocument userDocument = new UserDocument(updateUserDto.getEmail(), updateUserDto.getName(),
                updateUserDto.getSurname(), updateUserDto.getBirthDate(), updateUserDto.getSex(), updateUserDto.getState(),
                null, null, null);
        userDocument.setId(userId);

        new UserEventSubscriber<>(userTable, EVENT_BUS);
        new UserEventSubscriber<>(userDocument, EVENT_BUS);

        final UserUpdatedEvent updatedEvent = new UserUpdatedEvent(this);
        EVENT_BUS.post(updatedEvent);

        return ResponseEntity.ok().body("Data byla aktualizována");
    }

    /* methods called from events */
    public PersistentUser finishUserSaving(PersistentUser user) {
        if (user instanceof UserTable)
            return userRepository.save((UserTable) user);
        else
            return userDocumentRepository.save((UserDocument) user);
    }

    public PersistentUser finishUserUpdating(PersistentUser user) {
        if (user instanceof UserTable) {
            UserTable u = (UserTable) user;
            userRepository.updateUser(u.getIdUser(), u.getEmail(), u.getName(), u.getSurname(), u.getBirthDate(), u.getSex(), u.getState());
            return user;
        } else {
            return userDocumentRepository.save((UserDocument) user);
        }
    }

    private boolean userTableEqualsUpdateUserDto(UserTable table, UpdateUserDto dto) {
        return dto.getEmail().equals(table.getEmail()) &&
                dto.getName().equals(table.getName()) &&
                dto.getSurname().equals(table.getSurname()) &&
                dto.getBirthDate().equals(table.getBirthDate()) &&
                dto.getSex() == table.getSex() &&
                dto.getState() == table.getState();
    }

    public PersistentUser finishUserDeleting(PersistentUser persistentUser) { return null;
    }
}
