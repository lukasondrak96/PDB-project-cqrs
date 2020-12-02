package cz.vutbr.fit.pdb.projekt.api.commands.services;

import cz.vutbr.fit.pdb.projekt.api.commands.dtos.group.NewGroupDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.group.UpdateGroupDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.user.NewUserDto;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.CreatorEmbedded;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupState;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupTable;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserSex;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class GroupCommandServiceTest extends AbstractServiceTest {

    private final String TEST_NAME = "testName";
    private final String TEST_DESCRIPTION = "testDescription";
    private final GroupState TEST_STATE_PRIVATE = GroupState.PRIVATE;
    private final NewUserDto TEST_GROUP_CREATOR = new NewUserDto("test@test", "testName", "testSurname", new Date(300L), UserSex.FEMALE);

    private final String TEST_ADDITION_TO_CHANGE_STRING = "Addition";


    @Test
    void test_createGroup() {
        userCommandService.createUser(TEST_GROUP_CREATOR);
        int creatorId = userRepository.findAll().get(0).getId();
        CreatorEmbedded testCreatorEmbedded = new CreatorEmbedded(creatorId, "testName", "testSurname");
        NewGroupDto newGroupDto = new NewGroupDto(
                TEST_NAME, TEST_DESCRIPTION, TEST_STATE_PRIVATE, creatorId
        );

        groupCommandService.createGroup(newGroupDto);


        int createdGroupId = groupRepository.findAll().get(0).getId();
        Optional<GroupTable> createdGroupSqlOptional = groupRepository.findById(createdGroupId);
        Optional<GroupDocument> createdGroupNoSqlOptional = groupDocumentRepository.findById(createdGroupId);

        assertTrue(createdGroupSqlOptional.isPresent());
        assertTrue(createdGroupNoSqlOptional.isPresent());
        assertEquals(createdGroupId, createdGroupSqlOptional.get().getId());
        assertEquals(TEST_NAME, createdGroupSqlOptional.get().getName());
        assertEquals(TEST_DESCRIPTION, createdGroupSqlOptional.get().getDescription());
        assertEquals(TEST_STATE_PRIVATE, createdGroupSqlOptional.get().getState());
        assertEquals(TEST_GROUP_CREATOR.getEmail(), createdGroupSqlOptional.get().getUserReference().getEmail());

        assertEquals(new GroupDocument(createdGroupId, TEST_NAME, TEST_DESCRIPTION, TEST_STATE_PRIVATE,
                testCreatorEmbedded, null, null), createdGroupNoSqlOptional.get());
    }

    @Test
    void test_updateGroup() {
        userCommandService.createUser(TEST_GROUP_CREATOR);
        int creatorId = userRepository.findAll().get(0).getId();
        CreatorEmbedded testCreatorEmbedded = new CreatorEmbedded(creatorId, "testName", "testSurname");

        NewGroupDto newGroupDto = new NewGroupDto(
                TEST_NAME, TEST_DESCRIPTION, TEST_STATE_PRIVATE, creatorId
        );
        groupCommandService.createGroup(newGroupDto);
        UpdateGroupDto updateGroupDto = new UpdateGroupDto(
                TEST_NAME + TEST_ADDITION_TO_CHANGE_STRING, TEST_DESCRIPTION + TEST_ADDITION_TO_CHANGE_STRING
        );
        int createdGroupId = groupRepository.findAll().get(0).getId();


        groupCommandService.updateGroup(createdGroupId, updateGroupDto);


        Optional<GroupTable> updatedGroupSqlOptional = groupRepository.findById(createdGroupId);
        Optional<GroupDocument> updatedGroupNoSqlOptional = groupDocumentRepository.findById(createdGroupId);
        assertTrue(updatedGroupSqlOptional.isPresent());
        assertTrue(updatedGroupNoSqlOptional.isPresent());
        assertEquals(createdGroupId, updatedGroupSqlOptional.get().getId());
        assertEquals(TEST_NAME + TEST_ADDITION_TO_CHANGE_STRING, updatedGroupSqlOptional.get().getName());
        assertEquals(TEST_DESCRIPTION + TEST_ADDITION_TO_CHANGE_STRING, updatedGroupSqlOptional.get().getDescription());
        assertEquals(TEST_GROUP_CREATOR.getEmail(), updatedGroupSqlOptional.get().getUserReference().getEmail());

        assertEquals(new GroupDocument(createdGroupId, TEST_NAME + TEST_ADDITION_TO_CHANGE_STRING, TEST_DESCRIPTION + TEST_ADDITION_TO_CHANGE_STRING, TEST_STATE_PRIVATE,
                testCreatorEmbedded, null, null), updatedGroupNoSqlOptional.get());
    }

    @Test
    void test_deleteGroup() {
        userCommandService.createUser(TEST_GROUP_CREATOR);
        int creatorId = userRepository.findAll().get(0).getId();
        NewGroupDto newGroupDto = new NewGroupDto(
                TEST_NAME, TEST_DESCRIPTION, TEST_STATE_PRIVATE, creatorId
        );
        groupCommandService.createGroup(newGroupDto);
        int createdGroupId = groupRepository.findAll().get(0).getId();


        groupCommandService.deleteGroup(createdGroupId);


        Optional<GroupTable> updatedGroupSqlOptional = groupRepository.findById(createdGroupId);
        Optional<GroupDocument> updatedGroupNoSqlOptional = groupDocumentRepository.findById(createdGroupId);
        assertTrue(updatedGroupSqlOptional.isEmpty());
        assertTrue(updatedGroupNoSqlOptional.isEmpty());
    }

    @Test
    void test_changeGroupState() {
    }
}
