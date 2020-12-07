package cz.vutbr.fit.pdb.projekt.api.commands.services;

import cz.vutbr.fit.pdb.projekt.api.AbstractServiceTest;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.message.NewMessageDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.user.NewUserDto;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.embedded.ConversationEmbedded;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.embedded.MessageEmbedded;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.message.MessageTable;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserSex;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MessageCommandServiceTest extends AbstractServiceTest {

    private final NewUserDto TEST_SENDER = new NewUserDto("sender@sender", "senderName", "senderSurname", new Date(300L), UserSex.FEMALE);
    private final NewUserDto TEST_RECIPIENT = new NewUserDto("recipient@recipient", "recipientName", "recipientSurname", new Date(300L), UserSex.FEMALE);
    private final String TEST_TEXT = "test text of message";

    @Test
    void test_createMessage() {
        userCommandService.createUser(TEST_SENDER);
        UserTable sender = userRepository.findByEmail("sender@sender").get();
        int senderId = sender.getId();

        userCommandService.createUser(TEST_RECIPIENT);
        UserTable recipient = userRepository.findByEmail("recipient@recipient").get();
        int recipientId = recipient.getId();

        NewMessageDto newMessageDto = new NewMessageDto(TEST_TEXT, senderId, recipientId);


        messageCommandService.createMessage(newMessageDto);


    //tests if conversations were made
        UserDocument updatedSenderDocument = userDocumentRepository.findByEmail(sender.getEmail()).get();
        UserDocument updatedRecipientDocument = userDocumentRepository.findByEmail(recipient.getEmail()).get();
        assertTrue(updatedSenderDocument.getConversationsWithUser()
                        .stream()
                        .anyMatch(conversationEmbedded ->
                                conversationEmbedded.getId() == recipientId &&
                                conversationEmbedded.getName().equals(recipient.getName()) &&
                                conversationEmbedded.getSurname().equals(recipient.getSurname())
                        )
        );
        assertTrue(updatedRecipientDocument.getConversationsWithUser()
                .stream()
                .anyMatch(conversationEmbedded ->
                                conversationEmbedded.getId() == senderId &&
                                conversationEmbedded.getName().equals(sender.getName()) &&
                                conversationEmbedded.getSurname().equals(sender.getSurname())
                )
        );

        ConversationEmbedded sendersConversationWithReceiver =
                updatedSenderDocument.getConversationsWithUser().stream().filter(conversationEmbedded ->
                        conversationEmbedded.getId() == recipientId &&
                        conversationEmbedded.getName().equals(recipient.getName()) &&
                        conversationEmbedded.getSurname().equals(recipient.getSurname())
                ).collect(Collectors.toList()).get(0);

        ConversationEmbedded recipientConversationWithSender =
                updatedRecipientDocument.getConversationsWithUser().stream().filter(conversationEmbedded ->
                        conversationEmbedded.getId() == senderId &&
                        conversationEmbedded.getName().equals(sender.getName()) &&
                        conversationEmbedded.getSurname().equals(sender.getSurname())
                ).collect(Collectors.toList()).get(0);

    //tests if messages were added
        //sql
        List<MessageTable> messagesWithProvidedText = messageRepository.findByText(TEST_TEXT);
        List<MessageTable> filteredMessages = messagesWithProvidedText.stream()
                .filter(messageTable ->
                        messageTable.getRecipient().getId() == recipientId &&
                        messageTable.getSender().getId() == senderId
                ).collect(Collectors.toList());
        assertFalse(filteredMessages.isEmpty());

        //noSql
        MessageEmbedded messageInSender = sendersConversationWithReceiver.getMessages().get(sendersConversationWithReceiver.getMessages().size() - 1);
        assertEquals(TEST_TEXT, messageInSender.getText());
        assertTrue(messageInSender.isSent());

        MessageEmbedded messageInRecipient = recipientConversationWithSender.getMessages().get(recipientConversationWithSender.getMessages().size() - 1);
        assertEquals(TEST_TEXT, messageInRecipient.getText());
        assertFalse(messageInRecipient.isSent());
    }

    @Test
    void test_createMultipleMessages() {
        userCommandService.createUser(TEST_SENDER);
        UserTable sender = userRepository.findByEmail("sender@sender").get();
        int senderId = sender.getId();

        userCommandService.createUser(TEST_RECIPIENT);
        UserTable recipient = userRepository.findByEmail("recipient@recipient").get();
        int recipientId = recipient.getId();

        NewMessageDto firstMessage = new NewMessageDto(TEST_TEXT, senderId, recipientId);
        NewMessageDto secondMessage = new NewMessageDto(TEST_TEXT + "2", senderId, recipientId);
        NewMessageDto thirdMessage = new NewMessageDto(TEST_TEXT + "3", recipientId, senderId); //opposite direction


        messageCommandService.createMessage(firstMessage);
        messageCommandService.createMessage(secondMessage);
        messageCommandService.createMessage(thirdMessage);



        //tests if conversations were made
        UserDocument updatedSenderDocument = userDocumentRepository.findByEmail(sender.getEmail()).get();
        UserDocument updatedRecipientDocument = userDocumentRepository.findByEmail(recipient.getEmail()).get();
        assertTrue(updatedSenderDocument.getConversationsWithUser()
                .stream()
                .anyMatch(conversationEmbedded ->
                        conversationEmbedded.getId() == recipientId &&
                                conversationEmbedded.getName().equals(recipient.getName()) &&
                                conversationEmbedded.getSurname().equals(recipient.getSurname())
                )
        );
        assertTrue(updatedRecipientDocument.getConversationsWithUser()
                .stream()
                .anyMatch(conversationEmbedded ->
                        conversationEmbedded.getId() == senderId &&
                                conversationEmbedded.getName().equals(sender.getName()) &&
                                conversationEmbedded.getSurname().equals(sender.getSurname())
                )
        );

        ConversationEmbedded sendersConversationWithReceiver =
                updatedSenderDocument.getConversationsWithUser().stream().filter(conversationEmbedded ->
                        conversationEmbedded.getId() == recipientId &&
                                conversationEmbedded.getName().equals(recipient.getName()) &&
                                conversationEmbedded.getSurname().equals(recipient.getSurname())
                ).collect(Collectors.toList()).get(0);

        ConversationEmbedded recipientConversationWithSender =
                updatedRecipientDocument.getConversationsWithUser().stream().filter(conversationEmbedded ->
                        conversationEmbedded.getId() == senderId &&
                                conversationEmbedded.getName().equals(sender.getName()) &&
                                conversationEmbedded.getSurname().equals(sender.getSurname())
                ).collect(Collectors.toList()).get(0);

        //tests if messages were added
        //sql
        List<MessageTable> messagesWithProvidedText = messageRepository.findAll();
        List<MessageTable> filteredMessagesOfSender = messagesWithProvidedText
                .stream()
                .filter(messageTable ->
                        messageTable.getRecipient().getId() == recipientId &&
                                messageTable.getSender().getId() == senderId
                ).collect(Collectors.toList());
        assertFalse(filteredMessagesOfSender.isEmpty());
        assertEquals(2, filteredMessagesOfSender.size());

        List<MessageTable> filteredMessagesOfRecipient = messagesWithProvidedText
                .stream()
                .filter(messageTable ->
                        messageTable.getRecipient().getId() == senderId &&
                                messageTable.getSender().getId() == recipientId
                ).collect(Collectors.toList());

        assertFalse(filteredMessagesOfRecipient.isEmpty());
        assertEquals(1, filteredMessagesOfRecipient.size());


        //noSql
        List<MessageEmbedded> messagesBySenderInSenderDocument =
                sendersConversationWithReceiver.getMessages()
                        .stream()
                        .filter(MessageEmbedded::isSent)
                        .collect(Collectors.toList());
        assertEquals(2, messagesBySenderInSenderDocument.size());
        assertTrue(messagesBySenderInSenderDocument.stream().anyMatch(message -> message.getText().equals(TEST_TEXT)));
        assertTrue(messagesBySenderInSenderDocument.stream().anyMatch(message -> message.getText().equals(TEST_TEXT + "2")));

        List<MessageEmbedded> messagesByRecipientInSenderDocument =
                sendersConversationWithReceiver.getMessages()
                        .stream()
                        .filter(message -> !message.isSent())
                        .collect(Collectors.toList());
        assertEquals(1, messagesByRecipientInSenderDocument.size());
        assertEquals(TEST_TEXT + "3", messagesByRecipientInSenderDocument.get(0).getText());


        List<MessageEmbedded> messagesByRecipientInRecipientDocument =
                recipientConversationWithSender.getMessages()
                        .stream()
                        .filter(MessageEmbedded::isSent)
                        .collect(Collectors.toList());
        assertEquals(1, messagesByRecipientInRecipientDocument.size());
        assertEquals(TEST_TEXT + "3", messagesByRecipientInRecipientDocument.get(0).getText());

        List<MessageEmbedded> messagesBySenderInRecipientDocument =
                recipientConversationWithSender.getMessages()
                        .stream()
                        .filter(message -> !message.isSent())
                        .collect(Collectors.toList());
        assertEquals(2, messagesBySenderInRecipientDocument.size());
        assertTrue(messagesBySenderInRecipientDocument.stream().anyMatch(message -> message.getText().equals(TEST_TEXT)));
        assertTrue(messagesBySenderInRecipientDocument.stream().anyMatch(message -> message.getText().equals(TEST_TEXT + "2")));
    }
}
