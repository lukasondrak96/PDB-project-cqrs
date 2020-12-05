package cz.vutbr.fit.pdb.projekt.api.queries.services;


import cz.vutbr.fit.pdb.projekt.api.commands.dtos.comment.NewCommentDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.group.NewGroupDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.post.NewPostDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.user.NewUserDto;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.CommentEmbedded;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.CreatorEmbedded;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.MemberEmbedded;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.PostEmbedded;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.comment.CommentTable;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupState;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserSex;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GroupQueryServiceTest extends AbstractQueryServiceTest {

    private final String TEST_NAME = "testName";
    private final String TEST_DESCRIPTION = "testDescription";
    private final GroupState TEST_STATE_PRIVATE = GroupState.PRIVATE;
    private final GroupState TEST_STATE_PUBLIC = GroupState.PUBLIC;
    private final NewUserDto TEST_GROUP_CREATOR = new NewUserDto("group@creator", "groupCreatorName", "groupCreatorSurname", new Date(300L), UserSex.FEMALE);

    private final String TEST_ADDITION_TO_CHANGE_STRING = "Addition";
    private final NewUserDto TEST_GROUP_NEW_CREATOR = new NewUserDto("new@creator", "newCreator", "newCreatorSurname", new Date(300L), UserSex.FEMALE);
    private final NewUserDto TEST_GROUP_NEW_MEMBER = new NewUserDto("new@member", "newMember", "newMemberSurname", new Date(300L), UserSex.FEMALE);
    private final NewUserDto TEST_GROUP_NEW_MEMBER_2 = new NewUserDto("new@member2", "newMember2", "newMemberSurname2", new Date(300L), UserSex.FEMALE);
    private final NewUserDto TEST_GROUP_NEW_MEMBER_3 = new NewUserDto("new@member3", "newMember3", "newMemberSurname3", new Date(300L), UserSex.FEMALE);


    @Test
    public void test_getAllGroups() {
        userCommandService.createUser(TEST_GROUP_CREATOR);
        int creatorId = userRepository.findByEmail("group@creator").get().getId();

        NewGroupDto firstGroup = new NewGroupDto(
                TEST_NAME, TEST_DESCRIPTION, TEST_STATE_PRIVATE, creatorId
        );
        groupCommandService.createGroup(firstGroup);

        NewGroupDto secondGroup = new NewGroupDto(
                TEST_NAME + "2", TEST_DESCRIPTION  + "2", TEST_STATE_PRIVATE, creatorId
        );
        groupCommandService.createGroup(secondGroup);

        NewGroupDto thirdGroup = new NewGroupDto(
                TEST_NAME + "3", TEST_DESCRIPTION  + "3", TEST_STATE_PRIVATE, creatorId
        );
        groupCommandService.createGroup(thirdGroup);


        List<GroupDocument> allGroups = groupQueryService.getAllGroups().getBody();


        assertEquals(3, allGroups.size());
        assertEquals(TEST_NAME, allGroups.get(0).getName());
        assertEquals(TEST_NAME + "2", allGroups.get(1).getName());
        assertEquals(TEST_NAME + "3", allGroups.get(2).getName());
    }


    @Test
    public void test_getAllPostInGroup() {
        userCommandService.createUser(TEST_GROUP_CREATOR);
        int creatorId = userRepository.findByEmail("group@creator").get().getId();

        NewGroupDto group = new NewGroupDto(
                TEST_NAME, TEST_DESCRIPTION, TEST_STATE_PRIVATE, creatorId
        );
        groupCommandService.createGroup(group);
        int createdGroupId = groupRepository.findByName(TEST_NAME).get().getId();

        postCommandService.createPost(new NewPostDto("testPostTitle", "testPostText", creatorId, createdGroupId));
        postCommandService.createPost(new NewPostDto("testPostTitle2", "testPostText2", creatorId, createdGroupId));
        postCommandService.createPost(new NewPostDto("testPostTitle3", "testPostText3", creatorId, createdGroupId));


        List<PostEmbedded> allPostOfGroup = groupQueryService.getAllPostInGroup(createdGroupId).getBody();


        assertEquals(3, allPostOfGroup.size());
        assertEquals("testPostTitle", allPostOfGroup.get(0).getTitle());
        assertEquals("testPostTitle2", allPostOfGroup.get(1).getTitle());
        assertEquals("testPostTitle3", allPostOfGroup.get(2).getTitle());
    }

    @Test
    public void test_getGroupMembers() {
        userCommandService.createUser(TEST_GROUP_CREATOR);
        int creatorId = userRepository.findByEmail("group@creator").get().getId();

        userCommandService.createUser(TEST_GROUP_NEW_MEMBER);
        int member1Id = userRepository.findByEmail(TEST_GROUP_NEW_MEMBER.getEmail()).get().getId();
        userCommandService.createUser(TEST_GROUP_NEW_MEMBER_2);
        int member2Id = userRepository.findByEmail(TEST_GROUP_NEW_MEMBER_2.getEmail()).get().getId();
        userCommandService.createUser(TEST_GROUP_NEW_MEMBER_3);
        int member3Id = userRepository.findByEmail(TEST_GROUP_NEW_MEMBER_3.getEmail()).get().getId();

        NewGroupDto group = new NewGroupDto(
                TEST_NAME, TEST_DESCRIPTION, TEST_STATE_PRIVATE, creatorId
        );
        groupCommandService.createGroup(group);
        int createdGroupId = groupRepository.findByName(TEST_NAME).get().getId();

        groupCommandService.addGroupMember(createdGroupId, member1Id);
        groupCommandService.addGroupMember(createdGroupId, member2Id);
        groupCommandService.addGroupMember(createdGroupId, member3Id);


        List<MemberEmbedded> allMembers = groupQueryService.getGroupMembers(createdGroupId).getBody();


        assertEquals(3, allMembers.size());
        assertEquals(member1Id, allMembers.get(0).getId());
        assertEquals(TEST_GROUP_NEW_MEMBER.getName(), allMembers.get(0).getName());
        assertEquals(TEST_GROUP_NEW_MEMBER.getSurname(), allMembers.get(0).getSurname());

        assertEquals(member2Id, allMembers.get(1).getId());
        assertEquals(TEST_GROUP_NEW_MEMBER_2.getName(), allMembers.get(1).getName());
        assertEquals(TEST_GROUP_NEW_MEMBER_2.getSurname(), allMembers.get(1).getSurname());

        assertEquals(member3Id, allMembers.get(2).getId());
        assertEquals(TEST_GROUP_NEW_MEMBER_3.getName(), allMembers.get(2).getName());
        assertEquals(TEST_GROUP_NEW_MEMBER_3.getSurname(), allMembers.get(2).getSurname());

    }

    @Test
    public void test_getGroupAdmin() {
        userCommandService.createUser(TEST_GROUP_CREATOR);
        int creatorId = userRepository.findByEmail("group@creator").get().getId();

        NewGroupDto group = new NewGroupDto(
                TEST_NAME, TEST_DESCRIPTION, TEST_STATE_PRIVATE, creatorId
        );
        groupCommandService.createGroup(group);
        int createdGroupId = groupRepository.findByName(TEST_NAME).get().getId();


        CreatorEmbedded creator = groupQueryService.getGroupAdmin(createdGroupId).getBody();


        assertEquals(creatorId, creator.getId());
        assertEquals("groupCreatorName", creator.getName());
        assertEquals("groupCreatorSurname", creator.getSurname());
    }

    @Test
    public void test_getAllCommentsFromPost() {
        userCommandService.createUser(TEST_GROUP_CREATOR);
        int creatorId = userRepository.findByEmail("group@creator").get().getId();

        NewGroupDto group = new NewGroupDto(
                TEST_NAME, TEST_DESCRIPTION, TEST_STATE_PRIVATE, creatorId
        );
        groupCommandService.createGroup(group);
        int createdGroupId = groupRepository.findByName(TEST_NAME).get().getId();

        postCommandService.createPost(new NewPostDto("testPostTitle", "testPostText", creatorId, createdGroupId));
        int createdPostId = postRepository.findAll().get(0).getId();

        commentCommandService.createComment(new NewCommentDto("test text", creatorId, createdPostId));
        commentCommandService.createComment(new NewCommentDto("test text2", creatorId, createdPostId));
        commentCommandService.createComment(new NewCommentDto("test text3", creatorId, createdPostId));
        List<CommentTable> allCommentsSorted = findAllCommentsAndSortThem();
        CommentTable createdComment1 = allCommentsSorted.get(0);
        CommentTable createdComment2 = allCommentsSorted.get(1);
        CommentTable createdComment3 = allCommentsSorted.get(2);


        List<CommentEmbedded> allCommentsInPost = postQueryService.getAllCommentsFromPost(createdPostId).getBody();


        assertEquals(3, allCommentsInPost.size());
        assertEquals(createdComment1.getText(), allCommentsInPost.get(0).getText());
        assertEquals(createdComment1.getId(), allCommentsInPost.get(0).getId());

        assertEquals(createdComment2.getText(), allCommentsInPost.get(1).getText());
        assertEquals(createdComment2.getId(), allCommentsInPost.get(1).getId());

        assertEquals(createdComment3.getText(), allCommentsInPost.get(2).getText());
        assertEquals(createdComment3.getId(), allCommentsInPost.get(2).getId());
    }

    private List<CommentTable> findAllCommentsAndSortThem() {
        List<CommentTable> allComments = commentRepository.findAll();
        allComments.sort(Comparator.comparingInt(CommentTable::getId));
        return allComments;
    }
}
