package cz.vutbr.fit.pdb.projekt.api.commands.services;

import cz.vutbr.fit.pdb.projekt.api.AbstractServiceTest;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.comment.NewCommentDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.group.NewGroupDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.post.NewPostDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.user.NewUserDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.user.UpdateUserDto;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.PostEmbedded;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupState;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserSex;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserState;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserCommandServiceTest extends AbstractServiceTest {

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

    @Test
    void test_createUser() {
        NewUserDto newUserDto = new NewUserDto(
                TEST_EMAIL, TEST_NAME, TEST_SURNAME, TEST_BIRTH_DATE, TEST_SEX
        );


        userCommandService.createUser(newUserDto);


        int createdUserId = userRepository.findByEmail(TEST_EMAIL).get().getId();
        Optional<UserTable> createdUserSqlOptional = userRepository.findById(createdUserId);
        Optional<UserDocument> createdUserNoSqlOptional = userDocumentRepository.findById(createdUserId);
        assertTrue(createdUserSqlOptional.isPresent());
        assertTrue(createdUserNoSqlOptional.isPresent());
        assertEquals(createdUserId, createdUserSqlOptional.get().getId());
        assertEquals(TEST_NAME, createdUserSqlOptional.get().getName());
        assertEquals(TEST_SURNAME, createdUserSqlOptional.get().getSurname());
        assertEquals(TEST_EMAIL, createdUserSqlOptional.get().getEmail());
        assertEquals(0, createdUserSqlOptional.get().getBirthDate().compareTo(TEST_BIRTH_DATE));
        assertEquals(TEST_SEX, createdUserSqlOptional.get().getSex());
        assertEquals(STATE_ACTIVATED, createdUserSqlOptional.get().getState());

        assertEquals(createdUserNoSqlOptional.get(), new UserDocument(createdUserId, TEST_EMAIL, TEST_NAME, TEST_SURNAME, TEST_BIRTH_DATE,
                TEST_SEX, STATE_ACTIVATED, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
    }

    @Test
    void test_updateUser() {
        NewUserDto newUserDto = new NewUserDto(
                TEST_EMAIL, TEST_NAME, TEST_SURNAME, TEST_BIRTH_DATE, TEST_SEX
        );
        UpdateUserDto updateUserDto = new UpdateUserDto(
                TEST_EMAIL + TEST_ADDITION_TO_CHANGE_STRING, TEST_NAME + TEST_ADDITION_TO_CHANGE_STRING,
                TEST_SURNAME + TEST_ADDITION_TO_CHANGE_STRING, TEST_BIRTH_DATE_UPDATE, TEST_SEX_UPDATE
        );
        userCommandService.createUser(newUserDto);
        int createdUserId = userRepository.findByEmail(TEST_EMAIL).get().getId();


        userCommandService.updateUser(createdUserId, updateUserDto);


        Optional<UserTable> createdUserSqlOptional = userRepository.findById(createdUserId);
        Optional<UserDocument> createdUserNoSqlOptional = userDocumentRepository.findById(createdUserId);
        assertTrue(createdUserSqlOptional.isPresent());
        assertTrue(createdUserNoSqlOptional.isPresent());
        assertEquals(createdUserId, createdUserSqlOptional.get().getId());
        assertEquals(TEST_NAME + TEST_ADDITION_TO_CHANGE_STRING, createdUserSqlOptional.get().getName());
        assertEquals(TEST_SURNAME + TEST_ADDITION_TO_CHANGE_STRING, createdUserSqlOptional.get().getSurname());
        assertEquals(TEST_EMAIL + TEST_ADDITION_TO_CHANGE_STRING, createdUserSqlOptional.get().getEmail());
        assertEquals(0, createdUserSqlOptional.get().getBirthDate().compareTo(TEST_BIRTH_DATE_UPDATE));
        assertEquals(TEST_SEX_UPDATE, createdUserSqlOptional.get().getSex());
        assertEquals(STATE_ACTIVATED, createdUserSqlOptional.get().getState());

        assertEquals(new UserDocument(createdUserId, TEST_EMAIL + TEST_ADDITION_TO_CHANGE_STRING,
                TEST_NAME + TEST_ADDITION_TO_CHANGE_STRING, TEST_SURNAME + TEST_ADDITION_TO_CHANGE_STRING, TEST_BIRTH_DATE_UPDATE,
                TEST_SEX_UPDATE, STATE_ACTIVATED, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()), createdUserNoSqlOptional.get());
    }

    @Test
    void test_updateUser_updatesNameAndSurnameInGroups() {
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

        userCommandService.updateUser(createdUserId, updateUserDto);


        UserTable userTable = userRepository.findById(createdUserId).get();
        boolean allGroupCreatorsChangedInSql = groupRepository.findAll()
                .stream()
                .filter(group -> group.getCreator().getId() == userTable.getId())
                .allMatch(group ->
                        (group.getCreator().getName().equals(userTable.getName())) &&
                                (group.getCreator().getSurname().equals(userTable.getSurname()))
                );

        boolean allGroupCreatorsChangedInNoSql = groupDocumentRepository.findAll()
                .stream()
                .filter(group -> group.getCreator().getId() == userTable.getId())
                .allMatch(group ->
                        (group.getCreator().getName().equals(userTable.getName())) &&
                                (group.getCreator().getSurname().equals(userTable.getSurname()))
                );

        assertTrue(allGroupCreatorsChangedInSql);
        assertTrue(allGroupCreatorsChangedInNoSql);
    }

    @Test
    void test_updateUser_updatesNameAndSurnameInPosts() {
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


        userCommandService.updateUser(createdUserId, updateUserDto);


        UserTable userTable = userRepository.findById(createdUserId).get();
        boolean allPostsCreatorsChangedInSql = postRepository.findAll()
                .stream()
                .filter(post -> post.getCreator().getId() == userTable.getId())
                .allMatch(post ->
                        (post.getCreator().getName().equals(userTable.getName())) &&
                                (post.getCreator().getSurname().equals(userTable.getSurname()))
                );

        List<GroupDocument> allGroups = groupDocumentRepository.findAll();
        boolean allPostsCreatorsChangedInNoSql = true;
        for (GroupDocument groupDocument : allGroups) {
            allPostsCreatorsChangedInNoSql = groupDocument.getPosts()
                    .stream()
                    .filter(post -> post.getId() == userTable.getId())
                    .allMatch(post ->
                            (post.getCreator().getName().equals(userTable.getName())) &&
                                    (post.getCreator().getSurname().equals(userTable.getSurname()))

                    );
            if(!allPostsCreatorsChangedInNoSql)
                break;
        }

        assertTrue(allPostsCreatorsChangedInSql);
        assertTrue(allPostsCreatorsChangedInNoSql);
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
                .filter(comment -> comment.getCreator().getId() == userTable.getId())
                .allMatch(comment ->
                        (comment.getCreator().getName().equals(userTable.getName())) &&
                                (comment.getCreator().getSurname().equals(userTable.getSurname()))
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
