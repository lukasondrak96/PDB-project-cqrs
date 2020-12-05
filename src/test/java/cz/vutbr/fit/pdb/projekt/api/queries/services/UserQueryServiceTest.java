package cz.vutbr.fit.pdb.projekt.api.queries.services;

import cz.vutbr.fit.pdb.projekt.api.commands.dtos.comment.NewCommentDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.group.NewGroupDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.post.NewPostDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.user.NewUserDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.user.UpdateUserDto;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.PostEmbedded;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserQueryServiceTest extends AbstractQueryServiceTest {

    private final String TEST_NAME = "testName";
    private final String TEST_SURNAME = "testSurname";
    private final String TEST_EMAIL = "test@test";
    private final Date TEST_BIRTH_DATE = new Date(300L);
    private final UserSex TEST_SEX = UserSex.FEMALE;
    private final String TEST_ADDITION_TO_CHANGE_STRING = "Addition";
    private final Date TEST_BIRTH_DATE_UPDATE = new Date(500L);
    private final UserSex TEST_SEX_UPDATE = UserSex.MALE;

    private final UserState STATE_ACTIVATED = UserState.ACTIVATED;
    private final UserState STATE_DEACTIVATED = UserState.DEACTIVATED;
    private final GroupState TEST_GROUP_STATE = GroupState.PRIVATE;
    private final String TEST_TITLE = "testtitle";
    private final String TEST_TEXT = "testtext";

    private final String TEST_DESCRIPTION = "testDescription";
    private final GroupState TEST_STATE_PRIVATE = GroupState.PRIVATE;
    private final GroupState TEST_STATE_PUBLIC = GroupState.PUBLIC;

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
    void test_updateUser_updatesNameAndSurnameInComments() {
        NewUserDto newUserDto = new NewUserDto(
                TEST_EMAIL, TEST_NAME, TEST_SURNAME, TEST_BIRTH_DATE, TEST_SEX
        );
        UpdateUserDto updateUserDto = new UpdateUserDto(
                TEST_EMAIL + TEST_ADDITION_TO_CHANGE_STRING, TEST_NAME + TEST_ADDITION_TO_CHANGE_STRING,
                TEST_SURNAME + TEST_ADDITION_TO_CHANGE_STRING, TEST_BIRTH_DATE, TEST_SEX
        );
        userCommandService.createUser(newUserDto);
        int createdUserId = userRepository.findByEmail(TEST_EMAIL).get().getId();
        groupCommandService.createGroup(new NewGroupDto(TEST_NAME, null, TEST_GROUP_STATE, createdUserId));
        int createdGroupId = groupRepository.findByName(TEST_NAME).get().getId();
        postCommandService.createPost(new NewPostDto(TEST_TITLE, TEST_TEXT, createdUserId, createdGroupId));
        int createdPostId = postRepository.findAll().get(0).getId();
        commentCommandService.createComment(new NewCommentDto(TEST_TEXT, createdUserId, createdPostId));


        userCommandService.updateUser(createdUserId, updateUserDto);


        UserTable userTable = userRepository.findById(createdUserId).get();

        boolean allCommentsCreatorsChangedInSql = commentRepository.findAll()
                .stream()
                .filter(comment -> comment.getUserReference().getId() == userTable.getId())
                .allMatch(comment ->
                        (comment.getUserReference().getName().equals(userTable.getName())) &&
                                (comment.getUserReference().getSurname().equals(userTable.getSurname()))
                );

        List<GroupDocument> allGroups = groupDocumentRepository.findAll();
        boolean allCommentsCreatorsChangedInNoSql = true;
        for (GroupDocument groupDocument : allGroups) {
            for(PostEmbedded postEmbedded : groupDocument.getPosts()) {
                allCommentsCreatorsChangedInNoSql = postEmbedded.getComments()
                        .stream()
                        .filter(comment -> comment.getId() == userTable.getId())
                        .allMatch(comment ->
                                (comment.getCreator().getName().equals(userTable.getName())) &&
                                        (comment.getCreator().getSurname().equals(userTable.getSurname()))

                        );
                if(!allCommentsCreatorsChangedInNoSql)
                    break;
            }
        }

        assertTrue(allCommentsCreatorsChangedInSql);
        assertTrue(allCommentsCreatorsChangedInNoSql);
    }


    @Test
    void test_deactivate_and_activateUser() {
        NewUserDto newUserDto = new NewUserDto(
                TEST_EMAIL, TEST_NAME, TEST_SURNAME, TEST_BIRTH_DATE, TEST_SEX
        );
        userCommandService.createUser(newUserDto);
        int createdUserId = userRepository.findByEmail(TEST_EMAIL).get().getId();

        //deactivate
        userCommandService.deactivateUser(createdUserId);


        UserTable deactivatedUserTable = userRepository.findById(createdUserId).get();
        UserDocument deactivatedUserDocument = userDocumentRepository.findById(createdUserId).get();
        assertEquals(STATE_DEACTIVATED, deactivatedUserTable.getState());
        assertEquals(STATE_DEACTIVATED, deactivatedUserDocument.getState());

        //activate
        userCommandService.activateUser(createdUserId);


        UserTable activatedUserTable = userRepository.findById(createdUserId).get();
        UserDocument activatedUserDocument = userDocumentRepository.findById(createdUserId).get();
        assertEquals(STATE_ACTIVATED, activatedUserTable.getState());
        assertEquals(STATE_ACTIVATED, activatedUserDocument.getState());
    }
}
