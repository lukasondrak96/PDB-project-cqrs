package cz.vutbr.fit.pdb.projekt.api.commands.services;

import cz.vutbr.fit.pdb.projekt.api.AbstractServiceTest;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.comment.NewCommentDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.comment.UpdateCommentDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.group.NewGroupDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.post.NewPostDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.user.NewUserDto;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.PostEmbedded;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.comment.CommentTable;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupState;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.post.PostTable;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserSex;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CommentCommandServiceTest extends AbstractServiceTest {

    private final String TEST_TEXT = "testCommentText";

    private final NewUserDto TEST_GROUP_CREATOR = new NewUserDto("test@test", "testName", "testSurname", new Date(300L), UserSex.FEMALE);


    @Test
    void test_createComment() {
        userCommandService.createUser(TEST_GROUP_CREATOR);
        int creatorId = userRepository.findByEmail("test@test").get().getId();
        NewGroupDto newGroupDto = new NewGroupDto("testGroupName", "testGroupDescription", GroupState.PRIVATE, creatorId);
        groupCommandService.createGroup(newGroupDto);
        int createdGroupId = groupRepository.findByName("testGroupName").get().getId();
        groupCommandService.addGroupMember(createdGroupId, creatorId);
        postCommandService.createPost(new NewPostDto("testPostTitle", "testPostText", creatorId, createdGroupId));
        int createdPostId = postRepository.findAll().get(0).getId();


        commentCommandService.createComment(new NewCommentDto(TEST_TEXT, creatorId, createdPostId));


        int createdCommentId = commentRepository.findAll().get(0).getId();
        Optional<CommentTable> createdCommentOptional = commentRepository.findById(createdCommentId);
        assertTrue(createdCommentOptional.isPresent());
        CommentTable createdComment = createdCommentOptional.get();
        assertEquals(TEST_TEXT, createdComment.getText());
        assertEquals(creatorId, createdComment.getCreator().getId());
        assertEquals(createdPostId, createdComment.getPostReference().getId());

        GroupDocument groupWithCommentNoSqlOptional = groupDocumentRepository.findById(createdGroupId).get();
        assertEquals(1, groupWithCommentNoSqlOptional.getPosts().get(0).getComments().size());
    }

    @Test
    void test_updateComment() {
        userCommandService.createUser(TEST_GROUP_CREATOR);
        int creatorId = userRepository.findByEmail("test@test").get().getId();
        NewGroupDto newGroupDto = new NewGroupDto("testGroupName", "testGroupDescription", GroupState.PRIVATE, creatorId);
        groupCommandService.createGroup(newGroupDto);
        int createdGroupId = groupRepository.findByName("testGroupName").get().getId();
        groupCommandService.addGroupMember(createdGroupId, creatorId);
        postCommandService.createPost(new NewPostDto("testPostTitle", "testPostText", creatorId, createdGroupId));
        List<PostTable> allPosts = findAllPostsAndSortThem();
        int createdPostId = allPosts.get(0).getId();
        commentCommandService.createComment(new NewCommentDto(TEST_TEXT, creatorId, createdPostId));
        List<CommentTable> allComments = findAllCommentsAndSortThem();
        int createdCommentId = allComments.get(0).getId();

        UpdateCommentDto updateCommentDto = new UpdateCommentDto(TEST_TEXT + "updated");


        commentCommandService.updateComment(createdCommentId, updateCommentDto);


        int updatedCommentId = commentRepository.findAll().get(0).getId();
        Optional<CommentTable> updatedCommentOptional = commentRepository.findById(updatedCommentId);
        assertTrue(updatedCommentOptional.isPresent());
        CommentTable updatedComment = updatedCommentOptional.get();
        assertEquals(TEST_TEXT + "updated", updatedComment.getText());
        assertEquals(creatorId, updatedComment.getCreator().getId());
        assertEquals(createdPostId, updatedComment.getPostReference().getId());

        GroupDocument groupWithPostNoSqlOptional = groupDocumentRepository.findById(createdGroupId).get();
        assertEquals(1, groupWithPostNoSqlOptional.getPosts().size());
    }


    @Test
    void test_deletePost() {
        userCommandService.createUser(TEST_GROUP_CREATOR);
        int creatorId = userRepository.findByEmail("test@test").get().getId();
        NewGroupDto newGroupDto = new NewGroupDto("testGroupName", "testGroupDescription", GroupState.PRIVATE, creatorId);
        groupCommandService.createGroup(newGroupDto);
        int createdGroupId = groupRepository.findByName("testGroupName").get().getId();
        groupCommandService.addGroupMember(createdGroupId, creatorId);
        postCommandService.createPost(new NewPostDto("testPostTitle", "testPostText", creatorId, createdGroupId));
        int createdPostId = postRepository.findAll().get(0).getId();
        commentCommandService.createComment(new NewCommentDto(TEST_TEXT, creatorId, createdPostId));
        int createdCommentId = commentRepository.findAll().get(0).getId();


        commentCommandService.deleteComment(createdCommentId);


        Optional<CommentTable> deletedCommentOptional = commentRepository.findById(createdCommentId);
        assertTrue(deletedCommentOptional.isEmpty());
        assertTrue(commentRepository.findAll().isEmpty());

        GroupDocument updatedGroupDocument = groupDocumentRepository.findById(createdGroupId).get();
        assertTrue(updatedGroupDocument.getPosts().get(0).getComments().isEmpty());
    }

    @Test
    void test_deletePostWithMultiplePostsInGroup() {
        userCommandService.createUser(TEST_GROUP_CREATOR);
        int creatorId = userRepository.findByEmail("test@test").get().getId();
        NewGroupDto newGroupDto = new NewGroupDto("testGroupName", "testGroupDescription", GroupState.PRIVATE, creatorId);
        groupCommandService.createGroup(newGroupDto);
        int createdGroupId = groupRepository.findByName("testGroupName").get().getId();
        groupCommandService.addGroupMember(createdGroupId, creatorId);
        postCommandService.createPost(new NewPostDto("testPostTitle", "testPostText", creatorId, createdGroupId));
        int createdPostId = postRepository.findAll().get(0).getId();

        commentCommandService.createComment(new NewCommentDto(TEST_TEXT + "1", creatorId, createdPostId));
        commentCommandService.createComment(new NewCommentDto(TEST_TEXT + "2", creatorId, createdPostId));
        commentCommandService.createComment(new NewCommentDto(TEST_TEXT + "3", creatorId, createdPostId));

        List<CommentTable> allCommentsSorted = findAllCommentsAndSortThem();
        int createdComment1Id = allCommentsSorted.get(0).getId();
        int createdComment2Id = allCommentsSorted.get(1).getId();
        int createdComment3Id = allCommentsSorted.get(2).getId();


        commentCommandService.deleteComment(createdComment2Id);        //delete second comment in post


        Optional<CommentTable> firstCommentOptional = commentRepository.findById(createdComment1Id);
        Optional<CommentTable> deletedSecondCommentOptional = commentRepository.findById(createdComment2Id);
        Optional<CommentTable> thirdCommentOptional = commentRepository.findById(createdComment3Id);
        assertTrue(firstCommentOptional.isPresent());
        assertTrue(deletedSecondCommentOptional.isEmpty());
        assertTrue(thirdCommentOptional.isPresent());

        GroupDocument updatedGroupDocument = groupDocumentRepository.findById(createdGroupId).get();
        PostEmbedded postWithComments = updatedGroupDocument.getPosts().get(0);
        assertEquals(2, postWithComments.getComments().size());
        assertEquals(createdComment1Id, postWithComments.getComments().get(0).getId());
        assertEquals(createdComment3Id, postWithComments.getComments().get(1).getId());
    }


    private List<PostTable> findAllPostsAndSortThem() {
        List<PostTable> allPosts = postRepository.findAll();
        allPosts.sort(Comparator.comparingInt(PostTable::getId));
        return allPosts;
    }

    private List<CommentTable> findAllCommentsAndSortThem() {
        List<CommentTable> allComments = commentRepository.findAll();
        allComments.sort(Comparator.comparingInt(CommentTable::getId));
        return allComments;
    }

}

