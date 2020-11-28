package cz.vutbr.fit.pdb.projekt.api.commands.services;

import cz.vutbr.fit.pdb.projekt.api.commands.dtos.user.NewUserDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.user.UpdateUserDto;
import cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices.UserWithStateChangingService;
import cz.vutbr.fit.pdb.projekt.events.events.AbstractEvent;
import cz.vutbr.fit.pdb.projekt.events.events.OracleCreatedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.user.UserActivatedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.user.UserDeactivatedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.user.UserUpdatedEvent;
import cz.vutbr.fit.pdb.projekt.events.subscribers.user.MongoUserEventSubscriber;
import cz.vutbr.fit.pdb.projekt.events.subscribers.user.OracleUserEventSubscriber;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.objects.UserInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentUser;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocumentRepository;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.CreatorEmbedded;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocumentRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserState;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import lombok.AllArgsConstructor;
import org.greenrobot.eventbus.EventBus;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
@AllArgsConstructor
public class UserCommandService implements UserWithStateChangingService<PersistentUser> {

    private final UserRepository userRepository;
    private final UserDocumentRepository userDocumentRepository;
    private final GroupDocumentRepository groupDocumentRepository;
    private final MongoTemplate mongoTemplate;

    private static final EventBus EVENT_BUS = EventBus.getDefault();

    public ResponseEntity<?> createUser(NewUserDto newUserDto) {
        if (userRepository.countByEmail(newUserDto.getEmail()) != 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tento uživatel již existuje");
        }

        final UserTable userTable = new UserTable(newUserDto.getEmail(), newUserDto.getName(), newUserDto.getSurname(), newUserDto.getBirthDate(), newUserDto.getSex());

        OracleCreatedEvent<PersistentUser> createdEvent = new OracleCreatedEvent<>(userTable, this);
        subscribeChangeEventToOracleAndMongo(createdEvent);

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

        UserUpdatedEvent<PersistentUser> updatedEvent = new UserUpdatedEvent<>(userTable, this);
        subscribeChangeEventToOracleAndMongo(updatedEvent);

        return ResponseEntity.ok().body("Data byla aktualizována");
    }

    @Transactional
    public ResponseEntity<?> activateUser(int userId) {
        Optional<UserTable> userTableOptional = userRepository.findById(userId);
        if (userTableOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tento uživatel neexistuje");
        }

        UserTable userTable = userTableOptional.get();

        if (userTable.getState() == UserState.ACTIVATED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tento uživatel nemá deaktivovaný účet");
        }

        UserActivatedEvent<PersistentUser> userActivatedEvent = new UserActivatedEvent<>(userTable, this);
        subscribeChangeEventToOracleAndMongo(userActivatedEvent);

        return ResponseEntity.ok().body("Uživatel byl aktivován");
    }

    @Transactional
    public ResponseEntity<?> deactivateUser(int userId) {
        Optional<UserTable> userTableOptional = userRepository.findById(userId);
        if (userTableOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tento uživatel neexistuje");
        }

        UserTable userTable = userTableOptional.get();

        if (userTable.getState() == UserState.DEACTIVATED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tento uživatel nemá aktivovaný účet");
        }

        UserDeactivatedEvent<PersistentUser> userDeactivatedEvent = new UserDeactivatedEvent<>(userTable, this);
        subscribeChangeEventToOracleAndMongo(userDeactivatedEvent);

        return ResponseEntity.ok().body("Uživatel byl deaktivován");
    }

/* methods called from events */

    @Override
    public PersistentUser finishUpdating(PersistentUser user) {
        if (user instanceof UserTable) {
            return userRepository.save((UserTable) user);
        } else {
            UserDocument userDocument = (UserDocument) user;
            updateGroupCreatorsNameAndSurname(userDocument);
//            updatePostCreatorsNameAndSurname(userDocument); //todo uncomment when Post creation ready
//            updateCommentsCreatorsNameAndSurname(userDocument); //todo uncomment when Comments creation ready

            return userDocumentRepository.save(userDocument);
        }
    }

    @Override
    public PersistentUser finishSaving(PersistentUser user) {
        if (user instanceof UserTable)
            return userRepository.save((UserTable) user);
        else
            return userDocumentRepository.save((UserDocument) user);
    }

    @Override
    public PersistentUser finishActivating(PersistentUser persistentUser) {
        if (persistentUser instanceof UserTable) {
            UserTable userTable = (UserTable) persistentUser;
            userTable.setState(UserState.ACTIVATED);
            return userRepository.save(userTable);
        } else {
            return userDocumentRepository.save((UserDocument) persistentUser);
        }
    }

    @Override
    public PersistentUser finishDeactivating(PersistentUser persistentUser) {
        if (persistentUser instanceof UserTable) {
            UserTable userTable = (UserTable) persistentUser;
            userTable.setState(UserState.DEACTIVATED);
            return userRepository.save(userTable);
        } else {
            return userDocumentRepository.save((UserDocument) persistentUser);
        }
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

    /* private methods */

    private void subscribeChangeEventToOracleAndMongo(AbstractEvent<PersistentUser> event) {
        OracleUserEventSubscriber sqlSubscriber = new OracleUserEventSubscriber(EVENT_BUS);
        MongoUserEventSubscriber noSqlSubscriber = new MongoUserEventSubscriber(EVENT_BUS);
        EVENT_BUS.post(event);

        EVENT_BUS.unregister(sqlSubscriber);
        EVENT_BUS.unregister(noSqlSubscriber);
    }

    private boolean userTableEqualsUpdateUserDto(UserTable table, UpdateUserDto dto) {
        return dto.getEmail().equals(table.getEmail()) &&
                dto.getName().equals(table.getName()) &&
                dto.getSurname().equals(table.getSurname()) &&
                dto.getBirthDate().equals(table.getBirthDate()) &&
                dto.getSex() == table.getSex() &&
                dto.getState() == table.getState();
    }

    private boolean groupCreatorChangedName(UserDocument userDocument, CreatorEmbedded groupCreator) {
        return !groupCreator.getName().equals(userDocument.getName()) || !groupCreator.getSurname().equals(userDocument.getSurname());
    }

    private void updateGroupCreatorsNameAndSurname(UserDocument userDocument) {
        mongoTemplate.updateMulti(
                new Query(where("creator.id").is(userDocument.getId())),
                new Update()
                        .set("creator.name", userDocument.getName())
                        .set("creator.surname", userDocument.getSurname()),
                GroupDocument.class
        );
    }

    private void updatePostCreatorsNameAndSurname(UserDocument userDocument) {
        mongoTemplate.updateMulti(
                new Query(where("posts.creator.id").is(userDocument.getId())),
                new Update()
                        .set("posts.$.creator.name", userDocument.getName())
                        .set("posts.$.creator.surname", userDocument.getSurname()),
                GroupDocument.class
        );
    }

    private void updateCommentsCreatorsNameAndSurname(UserDocument userDocument) {
        mongoTemplate.updateMulti(
                new Query(where("posts.comments.creator.id").is(userDocument.getId())),
                new Update()
                        .set("posts.$.comments.$.creator.name", userDocument.getName())
                        .set("posts.$.comments.$.creator.surname", userDocument.getSurname()),
                GroupDocument.class
        );
    }

}
