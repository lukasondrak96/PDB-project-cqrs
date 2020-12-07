package cz.vutbr.fit.pdb.projekt.api.queries.services;

import cz.vutbr.fit.pdb.projekt.api.commands.dtos.group.NewGroupDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.message.NewMessageDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.user.NewUserDto;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.embedded.ConversationEmbedded;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.embedded.GroupEmbedded;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupState;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserSex;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserState;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserQueryServiceTest extends AbstractQueryServiceTest {

    private final String TEST_NAME = "testName";
    private final String TEST_SURNAME = "testSurname";
    private final String TEST_EMAIL = "test@test";
    private final Date TEST_BIRTH_DATE = new Date(300L);
    private final UserSex TEST_SEX = UserSex.FEMALE;
    private final String TEST_ADDITION_TO_CHANGE_STRING = "Addition";

    private final UserState STATE_ACTIVATED = UserState.ACTIVATED;
    private final UserState STATE_DEACTIVATED = UserState.DEACTIVATED;
    private final GroupState TEST_GROUP_STATE = GroupState.PRIVATE;
    private final String TEST_TITLE = "testtitle";
    private final String TEST_TEXT = "testtext";

    private final String TEST_DESCRIPTION = "testDescription";
    private final GroupState TEST_STATE_PRIVATE = GroupState.PRIVATE;

    private final NewUserDto TEST_SENDER = new NewUserDto("sender@sender", "senderName", "senderSurname", new Date(300L), UserSex.FEMALE);
    private final NewUserDto TEST_RECIPIENT = new NewUserDto("recipient@recipient", "recipientName", "recipientSurname", new Date(300L), UserSex.FEMALE);
    private final NewUserDto TEST_RECIPIENT_2 = new NewUserDto("recipient2@recipient2", "recipient2Name", "recipient2Surname", new Date(300L), UserSex.FEMALE);


    @Test
    void test_getAllUsers() {
        NewUserDto newUserDto = new NewUserDto(
                TEST_EMAIL, TEST_NAME, TEST_SURNAME, TEST_BIRTH_DATE, TEST_SEX
        );
        userCommandService.createUser(newUserDto);

        NewUserDto newUserDto2 = new NewUserDto(
                TEST_EMAIL + "2", TEST_NAME + "2", TEST_SURNAME + "2", TEST_BIRTH_DATE, TEST_SEX
        );
        userCommandService.createUser(newUserDto2);


        List<UserDocument> allUsers = userQueryService.getAllUsers().getBody();


        assertEquals(2, allUsers.size());
        assertEquals(TEST_EMAIL, allUsers.get(0).getEmail());
        assertEquals(TEST_EMAIL + "2", allUsers.get(1).getEmail());
    }

    @Test
    void test_getAllActiveUsers() {
        NewUserDto newUserDto = new NewUserDto(
                TEST_EMAIL, TEST_NAME, TEST_SURNAME, TEST_BIRTH_DATE, TEST_SEX
        );
        userCommandService.createUser(newUserDto);

        NewUserDto newUserDto2 = new NewUserDto(
                TEST_EMAIL + "2", TEST_NAME + "2", TEST_SURNAME + "2", TEST_BIRTH_DATE, TEST_SEX
        );
        userCommandService.createUser(newUserDto2);
        UserTable secondUser = userRepository.findByEmail(TEST_EMAIL + "2").get();
        userCommandService.deactivateUser(secondUser.getId());


        List<UserDocument> allActiveUsers = userQueryService.getAllActiveUsers().getBody();


        assertEquals(1, allActiveUsers.size());
        assertEquals(TEST_EMAIL, allActiveUsers.get(0).getEmail());
    }

    @Test
    void test_getInformationAboutUser() {
        NewUserDto newUserDto = new NewUserDto(
                TEST_EMAIL, TEST_NAME, TEST_SURNAME, TEST_BIRTH_DATE, TEST_SEX
        );
        userCommandService.createUser(newUserDto);
        int userId = userRepository.findByEmail(TEST_EMAIL).get().getId();


        ResponseEntity<UserDocument> responseEntity = userQueryService.getInformationAboutUser(userId);


        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(TEST_EMAIL, responseEntity.getBody().getEmail());
        assertEquals(TEST_NAME, responseEntity.getBody().getName());
        assertEquals(userId, responseEntity.getBody().getId());
    }

    @Test
    void test_getGroupsWhereUserIsAdmin() {
        NewUserDto newUserDto = new NewUserDto(
                TEST_EMAIL, TEST_NAME, TEST_SURNAME, TEST_BIRTH_DATE, TEST_SEX
        );
        userCommandService.createUser(newUserDto);
        int creatorId = userRepository.findByEmail(TEST_EMAIL).get().getId();

        NewGroupDto firstGroup = new NewGroupDto(
                TEST_NAME, TEST_DESCRIPTION, TEST_STATE_PRIVATE, creatorId
        );
        groupCommandService.createGroup(firstGroup);
        int firstGroupId = groupRepository.findByName(TEST_NAME).get().getId();

        NewGroupDto secondGroup = new NewGroupDto(
                TEST_NAME + "2", TEST_DESCRIPTION  + "2", TEST_STATE_PRIVATE, creatorId
        );
        groupCommandService.createGroup(secondGroup);
        int secondGroupId = groupRepository.findByName(TEST_NAME + "2").get().getId();

        NewUserDto anotherUserDto = new NewUserDto(
                TEST_EMAIL + "2", TEST_NAME + "2", TEST_SURNAME + "2", TEST_BIRTH_DATE, TEST_SEX
        );
        userCommandService.createUser(anotherUserDto);
        int anotherCreatorId = userRepository.findByEmail(TEST_EMAIL+ "2").get().getId();


        NewGroupDto thirdGroup = new NewGroupDto(
                TEST_NAME + "3", TEST_DESCRIPTION  + "3", TEST_STATE_PRIVATE, anotherCreatorId
        );
        groupCommandService.createGroup(thirdGroup);
        int thirdGroupId = groupRepository.findByName(TEST_NAME + "3").get().getId();


        ResponseEntity<List<GroupEmbedded>> responseEntity = userQueryService.getGroupsWhereUserIsAdmin(creatorId);


        assertEquals(2, responseEntity.getBody().size());
        assertEquals(firstGroupId, responseEntity.getBody().get(0).getId());
        assertEquals(secondGroupId, responseEntity.getBody().get(1).getId());
        assertEquals(TEST_NAME, responseEntity.getBody().get(0).getName());
        assertEquals(TEST_NAME + "2", responseEntity.getBody().get(1).getName());
    }

    @Test
    void test_getGroupsWhereUserIsMember() {
        NewUserDto newUserDto = new NewUserDto(
                TEST_EMAIL, TEST_NAME, TEST_SURNAME, TEST_BIRTH_DATE, TEST_SEX
        );
        userCommandService.createUser(newUserDto);
        int creatorId = userRepository.findByEmail(TEST_EMAIL).get().getId();

        NewGroupDto firstGroup = new NewGroupDto(
                TEST_NAME, TEST_DESCRIPTION, TEST_STATE_PRIVATE, creatorId
        );
        groupCommandService.createGroup(firstGroup);
        int firstGroupId = groupRepository.findByName(TEST_NAME).get().getId();

        NewGroupDto secondGroup = new NewGroupDto(
                TEST_NAME + "2", TEST_DESCRIPTION  + "2", TEST_STATE_PRIVATE, creatorId
        );
        groupCommandService.createGroup(secondGroup);
        int secondGroupId = groupRepository.findByName(TEST_NAME + "2").get().getId();

        NewUserDto groupsMember = new NewUserDto(
                TEST_EMAIL + "2", TEST_NAME + "2", TEST_SURNAME + "2", TEST_BIRTH_DATE, TEST_SEX
        );
        userCommandService.createUser(groupsMember);
        int groupsMemberId = userRepository.findByEmail(TEST_EMAIL + "2").get().getId();
        groupCommandService.addGroupMember(firstGroupId, groupsMemberId);
        groupCommandService.addGroupMember(secondGroupId, groupsMemberId);


        ResponseEntity<List<GroupEmbedded>> responseEntity = userQueryService.getGroupsWhereUserIsMember(groupsMemberId);


        assertEquals(2, responseEntity.getBody().size());
        assertEquals(firstGroupId, responseEntity.getBody().get(0).getId());
        assertEquals(secondGroupId, responseEntity.getBody().get(1).getId());
        assertEquals(TEST_NAME, responseEntity.getBody().get(0).getName());
        assertEquals(TEST_NAME + "2", responseEntity.getBody().get(1).getName());
    }

    @Test
    void test_getAllUserConversations() {
        userCommandService.createUser(TEST_SENDER);
        UserTable sender = userRepository.findByEmail(TEST_SENDER.getEmail()).get();
        int senderId = sender.getId();

        userCommandService.createUser(TEST_RECIPIENT);
        UserTable recipient1 = userRepository.findByEmail(TEST_RECIPIENT.getEmail()).get();
        int recipient1Id = recipient1.getId();

        userCommandService.createUser(TEST_RECIPIENT_2);
        UserTable recipient2 = userRepository.findByEmail(TEST_RECIPIENT_2.getEmail()).get();
        int recipient2Id = recipient2.getId();

        NewMessageDto messageToFirstRecipient = new NewMessageDto(TEST_TEXT, senderId, recipient1Id);
        NewMessageDto messageToSecondRecipient = new NewMessageDto(TEST_TEXT + "2", senderId, recipient2Id);
        messageCommandService.createMessage(messageToFirstRecipient);
        messageCommandService.createMessage(messageToSecondRecipient);


        List<ConversationEmbedded> allSendersConversations = userQueryService.getAllUserConversations(senderId).getBody();


        assertEquals(2, allSendersConversations.size());
        assertEquals(1, allSendersConversations.get(0).getMessages().size());
        assertEquals(1, allSendersConversations.get(1).getMessages().size());
    }

    @Test
    void test_getAllMessagesFromConversation() {
        userCommandService.createUser(TEST_SENDER);
        UserTable sender = userRepository.findByEmail(TEST_SENDER.getEmail()).get();
        int senderId = sender.getId();

        userCommandService.createUser(TEST_RECIPIENT);
        UserTable recipient1 = userRepository.findByEmail(TEST_RECIPIENT.getEmail()).get();
        int recipient1Id = recipient1.getId();

        NewMessageDto firstMessage = new NewMessageDto(TEST_TEXT, senderId, recipient1Id);
        NewMessageDto secondMessage = new NewMessageDto(TEST_TEXT + "2", senderId, recipient1Id);
        messageCommandService.createMessage(firstMessage);
        messageCommandService.createMessage(secondMessage);


        List<ConversationEmbedded> allSendersConversations = userQueryService.getAllUserConversations(senderId).getBody();


        assertEquals(1, allSendersConversations.size());
        assertEquals(2, allSendersConversations.get(0).getMessages().size());
    }

}
