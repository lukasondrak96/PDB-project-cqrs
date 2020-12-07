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
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.references.UserReference;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.embedded.ConversationEmbedded;
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

import java.util.ArrayList;
import java.util.List;
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
            addMessageToUsers((MessageEmbedded) message);
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

    private void addMessageToUsers(MessageEmbedded message) {

        UserReference senderInfo = message.getSender();
        UserReference recipientInfo = message.getRecipient();


        List<UserDocument> userDocumentWithWantedConversation = mongoTemplate.find(
                new Query(
                        where("id").is(senderInfo.getId())
                        .and("conversationsWithUser.id").is(recipientInfo.getId())
                        //UserDocument of sender with conversation with recipient
                ),
                UserDocument.class
        );

        if(userDocumentWithWantedConversation.isEmpty()) { // no conversation with this user yet
            message.setSent(true);
            ArrayList<MessageEmbedded> messagesInConversation = new ArrayList<>();
            messagesInConversation.add(message);
            mongoTemplate.updateFirst(
                    new Query(where("id").is(senderInfo.getId())),
                    new Update()
                            .push("conversationsWithUser", new ConversationEmbedded(
                                    recipientInfo.getId(), recipientInfo.getName(), recipientInfo.getSurname(), messagesInConversation)
                            ),
                    UserDocument.class
            );


            //the same for recipient
            messagesInConversation.get(0).setSent(false);
            mongoTemplate.updateFirst(
                    new Query(where("id").is(recipientInfo.getId())),
                    new Update()
                            .push("conversationsWithUser", new ConversationEmbedded(
                                    senderInfo.getId(), senderInfo.getName(), senderInfo.getSurname(), messagesInConversation)
                            ),
                    UserDocument.class
            );

        } else if(userDocumentWithWantedConversation.size() == 1) { //conversation found - just add message
            message.setSent(true);
            mongoTemplate.updateFirst(
                    new Query(
                            where("id").is(senderInfo.getId())
                                    .and("conversationsWithUser.id").is(recipientInfo.getId())
                    ),
                    new Update()
                            .push("conversationsWithUser.$.messages", message),
                    UserDocument.class
            );

            //the same for recipient
            message.setSent(false);
            mongoTemplate.updateFirst(
                    new Query(
                            where("id").is(recipientInfo.getId())
                                    .and("conversationsWithUser.id").is(senderInfo.getId())
                    ),
                    new Update()
                            .push("conversationsWithUser.$.messages", message),
                    UserDocument.class
            );

        } else {
            //incompatible state - do not save anything
        }


    }
}

