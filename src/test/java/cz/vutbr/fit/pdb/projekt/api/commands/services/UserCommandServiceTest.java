package cz.vutbr.fit.pdb.projekt.api.commands.services;

import cz.vutbr.fit.pdb.projekt.api.commands.dtos.group.NewGroupDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.post.NewPostDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.user.NewUserDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.user.UpdateUserDto;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocumentRepository;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocumentRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupState;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.post.PostRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserSex;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserState;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserCommandServiceTest {

    private final String TEST_NAME = "testName";
    private final String TEST_SURNAME = "testSurname";
    private final String TEST_EMAIL = "test@test";
    private final Date TEST_BIRTH_DATE = new Date(300L);
    private final UserSex TEST_SEX = UserSex.FEMALE;
    private final String TEST_ADDITION_TO_CHANGE_STRING = "Addition";
    private final Date TEST_BIRTH_DATE_UPDATE = new Date(500L);
    private final UserSex TEST_SEX_UPDATE = UserSex.MALE;

    @Autowired
    UserCommandService userCommandService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDocumentRepository userDocumentRepository;

    @Autowired
    GroupCommandService groupCommandService;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    GroupDocumentRepository groupDocumentRepository;

    private final UserState STATE_ACTIVATED = UserState.ACTIVATED;
    private final UserState STATE_DEACTIVATED = UserState.DEACTIVATED;
    private final GroupState TEST_GROUP_STATE = GroupState.PRIVATE;
    private final String TEST_TITLE = "testtitle";
    private final String TEST_TEXT = "testtext";
    @Autowired
    PostCommandService postCommandService;
    @Autowired
    PostRepository postRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userDocumentRepository.deleteAll();
        groupDocumentRepository.deleteAll();
        groupRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        userDocumentRepository.deleteAll();
        groupDocumentRepository.deleteAll();
        groupRepository.deleteAll();
    }

    @Test
    void test_createUser() {
        NewUserDto newUserDto = new NewUserDto(
                TEST_EMAIL, TEST_NAME, TEST_SURNAME, TEST_BIRTH_DATE, TEST_SEX
        );


        userCommandService.createUser(newUserDto);


        int createdUserId = userRepository.findAll().get(0).getId();
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
                TEST_SEX, STATE_ACTIVATED, null, null, null));
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
        int createdUserId = userRepository.findAll().get(0).getId();
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

        assertEquals(createdUserNoSqlOptional.get(), new UserDocument(createdUserId, TEST_EMAIL + TEST_ADDITION_TO_CHANGE_STRING,
                TEST_NAME + TEST_ADDITION_TO_CHANGE_STRING, TEST_SURNAME + TEST_ADDITION_TO_CHANGE_STRING, TEST_BIRTH_DATE_UPDATE,
                TEST_SEX_UPDATE, STATE_ACTIVATED, null, null, null));

    }

    @Test
    void test_updateUser_updatesNameAndSurnameInGroupAndPosts() {
        NewUserDto newUserDto = new NewUserDto(
                TEST_EMAIL, TEST_NAME, TEST_SURNAME, TEST_BIRTH_DATE, TEST_SEX
        );
        UpdateUserDto updateUserDto = new UpdateUserDto(
                TEST_EMAIL + TEST_ADDITION_TO_CHANGE_STRING, TEST_NAME + TEST_ADDITION_TO_CHANGE_STRING,
                TEST_SURNAME + TEST_ADDITION_TO_CHANGE_STRING, TEST_BIRTH_DATE, TEST_SEX
        );
        userCommandService.createUser(newUserDto);
        int createdUserId = userRepository.findAll().get(0).getId();
        groupCommandService.createGroup(new NewGroupDto(TEST_NAME, null, TEST_GROUP_STATE, createdUserId));
        int createdGroupId = groupRepository.findAll().get(0).getId();
        postCommandService.createPost(new NewPostDto(TEST_TITLE, TEST_TEXT, createdUserId, createdGroupId));


        userCommandService.updateUser(createdUserId, updateUserDto);


        UserTable userTable = userRepository.findById(createdUserId).get();
        boolean allGroupCreatorsChangedInSql = groupRepository.findAll()
                .stream()
                .filter(group -> group.getUserReference().getId() == userTable.getId())
                .allMatch(group ->
                        (group.getUserReference().getName().equals(userTable.getName())) &&
                                (group.getUserReference().getSurname().equals(userTable.getSurname()))
                );

        boolean allGroupCreatorsChangedInNoSql = groupDocumentRepository.findAll()
                .stream()
                .filter(group -> group.getCreator().getId() == userTable.getId())
                .allMatch(group ->
                        (group.getCreator().getName().equals(userTable.getName())) &&
                                (group.getCreator().getSurname().equals(userTable.getSurname()))
                );

        boolean allPostsCreatorsChangedInSql = postRepository.findAll()
                .stream()
                .filter(post -> post.getUserReference().getId() == userTable.getId())
                .allMatch(post ->
                        (post.getUserReference().getName().equals(userTable.getName())) &&
                                (post.getUserReference().getSurname().equals(userTable.getSurname()))
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
        }


        assertTrue(allGroupCreatorsChangedInSql);
        assertTrue(allGroupCreatorsChangedInNoSql);
        assertTrue(allPostsCreatorsChangedInSql);
        assertTrue(allPostsCreatorsChangedInNoSql);
    }

    @Test
    void test_deactivate_and_activateUser() {
        NewUserDto newUserDto = new NewUserDto(
                TEST_EMAIL, TEST_NAME, TEST_SURNAME, TEST_BIRTH_DATE, TEST_SEX
        );
        userCommandService.createUser(newUserDto);
        int createdUserId = userRepository.findAll().get(0).getId();
        ;


        userCommandService.deactivateUser(createdUserId);


        UserTable deactivatedUserTable = userRepository.findById(createdUserId).get();
        UserDocument deactivatedUserDocument = userDocumentRepository.findById(createdUserId).get();

        assertEquals(STATE_DEACTIVATED, deactivatedUserTable.getState());
        assertEquals(STATE_DEACTIVATED, deactivatedUserDocument.getState());


        userCommandService.activateUser(createdUserId);

        UserTable activatedUserTable = userRepository.findById(createdUserId).get();
        UserDocument activatedUserDocument = userDocumentRepository.findById(createdUserId).get();

        assertEquals(STATE_ACTIVATED, activatedUserTable.getState());
        assertEquals(STATE_ACTIVATED, activatedUserDocument.getState());
    }
}
