package cz.vutbr.fit.pdb.projekt.api.commands.services;

import cz.vutbr.fit.pdb.projekt.api.commands.dtos.message.NewMessageDto;
import cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices.CreateCommandService;
import cz.vutbr.fit.pdb.projekt.events.events.AbstractEvent;
import cz.vutbr.fit.pdb.projekt.events.events.OracleCreatedEvent;
import cz.vutbr.fit.pdb.projekt.events.subscribers.message.MongoMessageEventSubscriber;
import cz.vutbr.fit.pdb.projekt.events.subscribers.message.OracleMessageEventSubscriber;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.objects.MessageInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentMessage;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.embedded.MessageEmbedded;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.message.MessageRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.message.MessageTable;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import lombok.AllArgsConstructor;
import org.greenrobot.eventbus.EventBus;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
@AllArgsConstructor
public class MessageCommandService implements CreateCommandService<PersistentMessage> {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    private static final EventBus EVENT_BUS = EventBus.getDefault();

    public ResponseEntity<?> createMessage(NewMessageDto newMessageDto) {
        Optional<UserTable> userSenderOptional = userRepository.findById(newMessageDto.getSenderId());
        if (userSenderOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Odesílatel s tímto id neexistuje");
        }
        UserTable sender = userSenderOptional.get();

        Optional<UserTable> userRecipientOptional = userRepository.findById(newMessageDto.getRecipientId());
        if (userRecipientOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Příjemce s tímto id neexistuje");
        }
        UserTable recipient = userRecipientOptional.get();

        final MessageTable messageTable = new MessageTable(newMessageDto.getText(), sender, recipient);
        OracleCreatedEvent<PersistentMessage> oracleCreatedEvent = new OracleCreatedEvent<>(messageTable, this);
        subscribeEventToOracleAndMongo(oracleCreatedEvent);
        return ResponseEntity.ok().body("Zpráva byla vytvořena");
    }

    /* methods called from events */
    @Override
    public PersistentMessage assignFromTo(ObjectInterface objectInterface, PersistentMessage message) {
        MessageInterface persistentMessageInterface = (MessageInterface) message;
        MessageInterface messageInterface = (MessageInterface) objectInterface;
        if (message instanceof MessageTable || message instanceof MessageEmbedded) {
            persistentMessageInterface.setId(messageInterface.getId());
            persistentMessageInterface.setText(messageInterface.getText());
            persistentMessageInterface.setCreatedAt(messageInterface.getCreatedAt());
            persistentMessageInterface.setReadAt(messageInterface.getReadAt());
            persistentMessageInterface.setSender(messageInterface.getSender());
            persistentMessageInterface.setRecipient(messageInterface.getRecipient());
        }
        return (PersistentMessage) persistentMessageInterface;
    }

    @Override
    public PersistentMessage finishSaving(PersistentMessage message) {
        if (message instanceof MessageTable) {
            return messageRepository.save((MessageTable) message);
        } else {
            addMessageToSender((MessageEmbedded) message);
            addMessageToRecipient((MessageEmbedded) message);
            return message;
        }
    }

    /* private methods */
    private void subscribeEventToOracleAndMongo(AbstractEvent<PersistentMessage> event) {
        OracleMessageEventSubscriber sqlSubscriber = new OracleMessageEventSubscriber(EVENT_BUS);
        MongoMessageEventSubscriber noSqlSubscriber = new MongoMessageEventSubscriber(EVENT_BUS);
        EVENT_BUS.post(event);

        EVENT_BUS.unregister(sqlSubscriber);
        EVENT_BUS.unregister(noSqlSubscriber);
    }

    private void addMessageToSender(MessageEmbedded message) {
        mongoTemplate.updateMulti(
                new Query(where("id").is(message.getRecipient())),
                new Update()
                        .push("posts", message),
                UserDocument.class
        );
    }

    private void addMessageToRecipient(MessageEmbedded message) {
        mongoTemplate.updateMulti(
                new Query(where("id").is(message.getSender())),
                new Update()
                        .push("posts", message),
                UserDocument.class
        );
    }
}

