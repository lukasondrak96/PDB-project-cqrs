package cz.vutbr.fit.pdb.projekt.api.commands.services;

import cz.vutbr.fit.pdb.projekt.api.commands.dtos.group.NewGroupDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.group.UpdateGroupDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.user.NewUserDto;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.CreatorEmbedded;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupState;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupTable;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserSex;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class GroupCommandServiceTest extends AbstractServiceTest {

    private final String TEST_NAME = "testName";
    private final String TEST_DESCRIPTION = "testDescription";
    private final GroupState TEST_STATE_PRIVATE = GroupState.PRIVATE;
    private final GroupState TEST_STATE_PUBLIC = GroupState.PUBLIC;
    private final NewUserDto TEST_GROUP_CREATOR = new NewUserDto("test@test", "testName", "testSurname", new Date(300L), UserSex.FEMALE);

    private final String TEST_ADDITION_TO_CHANGE_STRING = "Addition";
    private final NewUserDto TEST_GROUP_NEW_CREATOR = new NewUserDto("new@creator", "newCreator", "newCreatorSurname", new Date(300L), UserSex.FEMALE);
    private final NewUserDto TEST_GROUP_NEW_MEMBER = new NewUserDto("new@member", "newMember", "newMemberSurname", new Date(300L), UserSex.FEMALE);


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
                testCreatorEmbedded, new ArrayList<>(), new ArrayList<>()), createdGroupNoSqlOptional.get());
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
                testCreatorEmbedded, new ArrayList<>(), new ArrayList<>()), updatedGroupNoSqlOptional.get());
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


        Optional<GroupTable> deletedGroupSqlOptional = groupRepository.findById(createdGroupId);
        Optional<GroupDocument> deletedGroupNoSqlOptional = groupDocumentRepository.findById(createdGroupId);
        assertTrue(deletedGroupSqlOptional.isEmpty());
        assertTrue(deletedGroupNoSqlOptional.isEmpty());
    }

    @Test
    void test_deletePostWithMultiplePostsInGroup() {
        userCommandService.createUser(TEST_GROUP_CREATOR);
        int creatorId = userRepository.findAll().get(0).getId();

        NewGroupDto firstGroup = new NewGroupDto(
                TEST_NAME, TEST_DESCRIPTION, TEST_STATE_PRIVATE, creatorId
        );
        groupCommandService.createGroup(firstGroup);
        int firstGroupId = groupRepository.findAll().get(0).getId();

        NewGroupDto secondGroup = new NewGroupDto(
                TEST_NAME + "2", TEST_DESCRIPTION  + "2", TEST_STATE_PRIVATE, creatorId
        );
        groupCommandService.createGroup(secondGroup);
        int secondGroupId = groupRepository.findAll().get(1).getId();

        NewGroupDto thirdGroup = new NewGroupDto(
                TEST_NAME + "3", TEST_DESCRIPTION  + "3", TEST_STATE_PRIVATE, creatorId
        );
        groupCommandService.createGroup(thirdGroup);
        int thirdGroupId = groupRepository.findAll().get(2).getId();


        groupCommandService.deleteGroup(secondGroupId); //delete second group


        Optional<GroupTable> firstGroupSqlOptional = groupRepository.findById(firstGroupId);
        Optional<GroupDocument> firstGroupNoSqlOptional = groupDocumentRepository.findById(firstGroupId);
        assertTrue(firstGroupSqlOptional.isPresent());
        assertTrue(firstGroupNoSqlOptional.isPresent());

        Optional<GroupTable> secondGroupSqlOptional = groupRepository.findById(secondGroupId);
        Optional<GroupDocument> secondGroupNoSqlOptional = groupDocumentRepository.findById(secondGroupId);
        assertTrue(secondGroupSqlOptional.isEmpty());
        assertTrue(secondGroupNoSqlOptional.isEmpty());

        Optional<GroupTable> thirdGroupSqlOptional = groupRepository.findById(thirdGroupId);
        Optional<GroupDocument> thirdGroupNoSqlOptional = groupDocumentRepository.findById(thirdGroupId);
        assertTrue(thirdGroupSqlOptional.isPresent());
        assertTrue(thirdGroupNoSqlOptional.isPresent());
    }



    @Test
    void test_changeGroupState_private_to_public() {
        userCommandService.createUser(TEST_GROUP_CREATOR);
        int creatorId = userRepository.findAll().get(0).getId();
        NewGroupDto newGroupDto = new NewGroupDto(
                TEST_NAME, TEST_DESCRIPTION, TEST_STATE_PRIVATE, creatorId
        );
        groupCommandService.createGroup(newGroupDto);
        int createdGroupId = groupRepository.findAll().get(0).getId();

        groupCommandService.changeGroupState(createdGroupId, GroupState.PUBLIC);


        Optional<GroupTable> updatedGroupSqlOptional = groupRepository.findById(createdGroupId);
        Optional<GroupDocument> updatedGroupNoSqlOptional = groupDocumentRepository.findById(createdGroupId);
        assertTrue(updatedGroupSqlOptional.isPresent());
        assertTrue(updatedGroupNoSqlOptional.isPresent());
        assertEquals(GroupState.PUBLIC, updatedGroupSqlOptional.get().getState());
        assertEquals(GroupState.PUBLIC, updatedGroupNoSqlOptional.get().getState());
    }

    @Test
    void test_changeGroupState_private_to_archived() {
        userCommandService.createUser(TEST_GROUP_CREATOR);
        int creatorId = userRepository.findAll().get(0).getId();
        NewGroupDto newGroupDto = new NewGroupDto(
                TEST_NAME, TEST_DESCRIPTION, TEST_STATE_PRIVATE, creatorId
        );
        groupCommandService.createGroup(newGroupDto);
        int createdGroupId = groupRepository.findAll().get(0).getId();

        groupCommandService.changeGroupState(createdGroupId, GroupState.ARCHIVED);


        Optional<GroupTable> updatedGroupSqlOptional = groupRepository.findById(createdGroupId);
        Optional<GroupDocument> updatedGroupNoSqlOptional = groupDocumentRepository.findById(createdGroupId);
        assertTrue(updatedGroupSqlOptional.isPresent());
        assertTrue(updatedGroupNoSqlOptional.isPresent());
        assertEquals(GroupState.ARCHIVED, updatedGroupSqlOptional.get().getState());
        assertEquals(GroupState.ARCHIVED, updatedGroupNoSqlOptional.get().getState());
    }

    @Test
    void test_changeGroupState_public_to_private() {
        userCommandService.createUser(TEST_GROUP_CREATOR);
        int creatorId = userRepository.findAll().get(0).getId();
        NewGroupDto newGroupDto = new NewGroupDto(
                TEST_NAME, TEST_DESCRIPTION, TEST_STATE_PUBLIC, creatorId
        );
        groupCommandService.createGroup(newGroupDto);
        int createdGroupId = groupRepository.findAll().get(0).getId();


        groupCommandService.changeGroupState(createdGroupId, GroupState.PRIVATE);


        Optional<GroupTable> updatedGroupSqlOptional = groupRepository.findById(createdGroupId);
        Optional<GroupDocument> updatedGroupNoSqlOptional = groupDocumentRepository.findById(createdGroupId);
        assertTrue(updatedGroupSqlOptional.isPresent());
        assertTrue(updatedGroupNoSqlOptional.isPresent());
        assertEquals(GroupState.PRIVATE, updatedGroupSqlOptional.get().getState());
        assertEquals(GroupState.PRIVATE, updatedGroupNoSqlOptional.get().getState());
    }


    @Test
    void test_changeGroupAdmin() {
        userCommandService.createUser(TEST_GROUP_CREATOR);
        UserTable groupCreator = userRepository.findAll().get(0);
        NewGroupDto newGroupDto = new NewGroupDto(
                TEST_NAME, TEST_DESCRIPTION, TEST_STATE_PUBLIC, groupCreator.getId()
        );
        groupCommandService.createGroup(newGroupDto);
        int createdGroupId = groupRepository.findAll().get(0).getId();
        userCommandService.createUser(TEST_GROUP_NEW_CREATOR);
        UserTable newAdmin = userRepository.findAll().get(1);


        groupCommandService.changeGroupAdmin(createdGroupId, newAdmin.getId());


        Optional<GroupTable> updatedGroupSqlOptional = groupRepository.findById(createdGroupId);
        Optional<GroupDocument> updatedGroupNoSqlOptional = groupDocumentRepository.findById(createdGroupId);
        assertTrue(updatedGroupSqlOptional.isPresent());
        assertTrue(updatedGroupNoSqlOptional.isPresent());
        GroupTable updatedGroupTable = updatedGroupSqlOptional.get();
        GroupDocument updatedGroupDocument = updatedGroupNoSqlOptional.get();
        assertEquals(newAdmin.getId(), updatedGroupTable.getUserReference().getId());
        assertEquals(newAdmin.getId(), updatedGroupDocument.getCreator().getId());
        assertEquals(newAdmin.getName(), updatedGroupTable.getUserReference().getName());
        assertEquals(newAdmin.getName(), updatedGroupDocument.getCreator().getName());
        assertEquals(newAdmin.getSurname(), updatedGroupTable.getUserReference().getSurname());
        assertEquals(newAdmin.getSurname(), updatedGroupDocument.getCreator().getSurname());

        UserDocument newAdminDocument = userDocumentRepository.findById(newAdmin.getId()).get();
        assertEquals(1, newAdminDocument.getGroupsAdmin().size());

        UserDocument oldCreatorDocument = userDocumentRepository.findById(groupCreator.getId()).get();
        assertTrue(oldCreatorDocument.getGroupsAdmin().isEmpty());

    }

    @Test
    void test_addGroupMember() {
        userCommandService.createUser(TEST_GROUP_CREATOR);
        UserTable groupCreator = userRepository.findAll().get(0);
        NewGroupDto newGroupDto = new NewGroupDto(
                TEST_NAME, TEST_DESCRIPTION, TEST_STATE_PUBLIC, groupCreator.getId()
        );
        groupCommandService.createGroup(newGroupDto);
        int createdGroupId = groupRepository.findAll().get(0).getId();
        userCommandService.createUser(TEST_GROUP_NEW_MEMBER);
        UserTable newMember = userRepository.findAll().get(1);


        groupCommandService.addGroupMember(createdGroupId, newMember.getId());


        Optional<GroupTable> updatedGroupSqlOptional = groupRepository.findById(createdGroupId);
        Optional<GroupDocument> updatedGroupNoSqlOptional = groupDocumentRepository.findById(createdGroupId);
        assertTrue(updatedGroupSqlOptional.isPresent());
        assertTrue(updatedGroupNoSqlOptional.isPresent());
        GroupTable updatedGroupTable = updatedGroupSqlOptional.get();
        GroupDocument updatedGroupDocument = updatedGroupNoSqlOptional.get();
        assertEquals(newMember.getId(), updatedGroupTable.getUsers().get(0).getId());
        assertEquals(newMember.getId(), updatedGroupDocument.getMembers().get(0).getId());
        assertEquals(newMember.getName(), updatedGroupTable.getUsers().get(0).getName());
        assertEquals(newMember.getName(), updatedGroupDocument.getMembers().get(0).getName());
        assertEquals(newMember.getSurname(), updatedGroupTable.getUsers().get(0).getSurname());
        assertEquals(newMember.getSurname(), updatedGroupDocument.getMembers().get(0).getSurname());

        UserDocument newMemberDocument = userDocumentRepository.findById(newMember.getId()).get();
        assertEquals(1, newMemberDocument.getGroupsMember().size());

    }

    @Test
    void test_removeGroupMember() {
        userCommandService.createUser(TEST_GROUP_CREATOR);
        UserTable groupCreator = userRepository.findAll().get(0);
        NewGroupDto newGroupDto = new NewGroupDto(
                TEST_NAME, TEST_DESCRIPTION, TEST_STATE_PUBLIC, groupCreator.getId()
        );
        groupCommandService.createGroup(newGroupDto);
        int createdGroupId = groupRepository.findAll().get(0).getId();
        userCommandService.createUser(TEST_GROUP_NEW_MEMBER);
        UserTable member = userRepository.findAll().get(1);
        groupCommandService.addGroupMember(createdGroupId, member.getId());


        groupCommandService.removeGroupMember(createdGroupId, member.getId());


        Optional<GroupTable> updatedGroupSqlOptional = groupRepository.findById(createdGroupId);
        Optional<GroupDocument> updatedGroupNoSqlOptional = groupDocumentRepository.findById(createdGroupId);
        assertTrue(updatedGroupSqlOptional.isPresent());
        assertTrue(updatedGroupNoSqlOptional.isPresent());
        GroupTable updatedGroupTable = updatedGroupSqlOptional.get();
        GroupDocument updatedGroupDocument = updatedGroupNoSqlOptional.get();
        assertTrue(updatedGroupTable.getUsers().isEmpty());
        assertTrue(updatedGroupDocument.getMembers().isEmpty());

        UserDocument newMemberDocument = userDocumentRepository.findById(member.getId()).get();
        assertEquals(0, newMemberDocument.getGroupsMember().size());

    }
}
