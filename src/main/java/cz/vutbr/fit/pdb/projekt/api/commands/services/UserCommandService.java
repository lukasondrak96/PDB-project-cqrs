package cz.vutbr.fit.pdb.projekt.api.commands.services;

import cz.vutbr.fit.pdb.projekt.api.commands.dtos.user.NewUserDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.user.UpdateUserDto;
import cz.vutbr.fit.pdb.projekt.events.events.ConfirmedEventAdapter;
import cz.vutbr.fit.pdb.projekt.events.events.OracleCreatedEvent;
import cz.vutbr.fit.pdb.projekt.events.subscribers.user.MongoUserEventSubscriber;
import cz.vutbr.fit.pdb.projekt.events.subscribers.user.OracleUserChangeEventSubscriber;
import cz.vutbr.fit.pdb.projekt.events.subscribers.user.OracleUserCreateEventSubscriber;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.objects.UserInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentUser;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocumentRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import lombok.AllArgsConstructor;
import org.greenrobot.eventbus.EventBus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserCommandService implements CommandService<PersistentUser> {

    private final UserRepository userRepository;
    private final UserDocumentRepository userDocumentRepository;
    private static final EventBus EVENT_BUS = EventBus.getDefault();

    public ResponseEntity<?> createUser(NewUserDto newUserDto) {
        if (userRepository.countByEmail(newUserDto.getEmail()) != 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tento uživatel již existuje");
        }

        final UserTable userTable = new UserTable(newUserDto.getEmail(), newUserDto.getName(), newUserDto.getSurname(), newUserDto.getBirthDate(), newUserDto.getSex());

        OracleUserCreateEventSubscriber oracleSubscriber = new OracleUserCreateEventSubscriber(EVENT_BUS);
        MongoUserEventSubscriber mongoSubscriber = new MongoUserEventSubscriber(EVENT_BUS);

        EVENT_BUS.post(new OracleCreatedEvent<>(userTable, this));
        EVENT_BUS.unregister(oracleSubscriber);
        EVENT_BUS.unregister(mongoSubscriber);

        return ResponseEntity.ok().body("Účet byl vytvořen");
    }

    @Transactional
    public ResponseEntity<?> updateUser(Integer userId, UpdateUserDto updateUserDto) {
        Optional<UserTable> userTableOptional = userRepository.findById(userId);
        if(userTableOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tento uživatel neexistuje");

        UserTable oldUserTable = userTableOptional.get();

        if (userTableEqualsUpdateUserDto(oldUserTable, updateUserDto)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nebyl nalezen záznam pro editaci");
        }

        final UserTable userTable = new UserTable(userId, updateUserDto.getEmail(), updateUserDto.getName(),
                updateUserDto.getSurname(), updateUserDto.getBirthDate(), updateUserDto.getSex(), updateUserDto.getState());


        OracleUserChangeEventSubscriber sqlSubscriber = new OracleUserChangeEventSubscriber(EVENT_BUS);
        MongoUserEventSubscriber noSqlSubscriber = new MongoUserEventSubscriber(EVENT_BUS);

        OracleCreatedEvent<PersistentUser> createdEvent = new OracleCreatedEvent<>(userTable, this);
        ConfirmedEventAdapter<PersistentUser> adapterOfCreatedEvent = new ConfirmedEventAdapter<>(createdEvent);

        EVENT_BUS.post(adapterOfCreatedEvent);

        EVENT_BUS.unregister(sqlSubscriber);
        EVENT_BUS.unregister(noSqlSubscriber);

        return ResponseEntity.ok().body("Data byla aktualizována");
    }
//
//    @Transactional
//    public ResponseEntity<?> activateUser(String userId) {
//        Optional<UserDocument> userDocumentOptional = userDocumentRepository.findById(userId);
//        if (userDocumentOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tento uživatel neexistuje");
//        }
//        UserDocument doc = userDocumentOptional.get();
//        UpdateUserDto updateUserDto = new UpdateUserDto(doc.getEmail(), doc.getName(), doc.getSurname(), doc.getBirthDate(), doc.getSex(), UserState.ACTIVATED);
//        return updateUser(userId, updateUserDto);
//    }
//
//    @Transactional
//    public ResponseEntity<?> deactivateUser(String userId) {
//        Optional<UserDocument> userDocumentOptional = userDocumentRepository.findById(userId);
//        if (userDocumentOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tento uživatel neexistuje");
//        }
//        UserDocument doc = userDocumentOptional.get();
//        UpdateUserDto updateUserDto = new UpdateUserDto(doc.getEmail(), doc.getName(), doc.getSurname(), doc.getBirthDate(), doc.getSex(), UserState.DEACTIVATED);
//        return updateUser(userId, updateUserDto);
//    }

//    /* methods called from events */
//    @Override
//    public PersistentUser finishSaving(PersistentUser user) {
//        if (user instanceof UserTable)
//            return userRepository.save((UserTable) user);
//        else
//            return userDocumentRepository.save((UserDocument) user);
//    }
//
//    @Override
//    public PersistentUser finishUpdating(PersistentUser user) {
//        //todo muzem pouzit finishUserSaving misto tohoto, je to stejny a fakci to jen se save misto toho silenyho update (snad :D )
//        if (user instanceof UserTable) {
////            UserTable u = (UserTable) user;
////            userRepository.updateUser(u.getIdUser(), u.getEmail(), u.getName(), u.getSurname(), u.getBirthDate(), u.getSex(), u.getState());
////            return user;
//            return userRepository.save((UserTable) user);
//        } else {
//            return userDocumentRepository.save((UserDocument) user);
//        }
//    }
//
//    @Override
//    public PersistentUser finishDeleting(PersistentUser user) {
//        return null;
//    }
//
//
//    /* private methods */

    private boolean userTableEqualsUpdateUserDto(UserTable table, UpdateUserDto dto) {
        return dto.getEmail().equals(table.getEmail()) &&
                dto.getName().equals(table.getName()) &&
                dto.getSurname().equals(table.getSurname()) &&
                dto.getBirthDate().equals(table.getBirthDate()) &&
                dto.getSex() == table.getSex() &&
                dto.getState() == table.getState();
    }


    @Override
    public PersistentUser assignFromTo(ObjectInterface objectInterface, PersistentUser user) {
        UserInterface persistentUserInterface = (UserInterface) user;
        UserInterface userInterface = (UserInterface) objectInterface;
        if(user instanceof UserTable || user instanceof UserDocument) {
            persistentUserInterface.setId(userInterface.getId());
            persistentUserInterface.setName(userInterface.getName());
            persistentUserInterface.setSurname(userInterface.getSurname());
            persistentUserInterface.setEmail(userInterface.getEmail());
            persistentUserInterface.setBirthDate(userInterface.getBirthDate());
            persistentUserInterface.setSex(userInterface.getSex());
            persistentUserInterface.setState(userInterface.getState());
        }
        return (PersistentUser) persistentUserInterface;
    }

    @Override
    public PersistentUser save(PersistentUser user) {
        if (user instanceof UserTable)
            return userRepository.save((UserTable) user);
        else
            return userDocumentRepository.save((UserDocument) user);
    }
}
