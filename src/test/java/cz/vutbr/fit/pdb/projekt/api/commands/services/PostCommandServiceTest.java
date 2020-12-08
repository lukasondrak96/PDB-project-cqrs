package cz.vutbr.fit.pdb.projekt.api.commands.services;

import cz.vutbr.fit.pdb.projekt.api.AbstractServiceTest;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.group.NewGroupDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.post.NewPostDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.post.UpdatePostDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.user.NewUserDto;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocument;
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
class PostCommandServiceTest extends AbstractServiceTest {

    private final String TEST_TITLE = "testPostTitle";
    private final String TEST_TEXT = "testPostText";

    private final NewUserDto TEST_GROUP_CREATOR = new NewUserDto("test@test", "testName", "testSurname", new Date(300L), UserSex.FEMALE);


    @Test
    void test_createPost() {
        userCommandService.createUser(TEST_GROUP_CREATOR);
        int creatorId = userRepository.findByEmail("test@test").get().getId();
        NewGroupDto newGroupDto = new NewGroupDto("testGroupName", "testGroupDescription", GroupState.PRIVATE, creatorId);
        groupCommandService.createGroup(newGroupDto);
        int createdGroupId = groupRepository.findByName("testGroupName").get().getId();


        postCommandService.createPost(new NewPostDto(TEST_TITLE, TEST_TEXT, creatorId, createdGroupId));


        int createdPostId = postRepository.findAll().get(0).getId();
        Optional<PostTable> createdPostOptional = postRepository.findById(createdPostId);
        assertTrue(createdPostOptional.isPresent());
        PostTable createdPost = createdPostOptional.get();
        assertEquals(TEST_TITLE, createdPost.getTitle());
        assertEquals(TEST_TEXT, createdPost.getText());
        assertEquals(creatorId, createdPost.getCreator().getId());
        assertEquals(createdGroupId, createdPost.getGroupReference().getId());

        GroupDocument groupWithPostNoSqlOptional = groupDocumentRepository.findById(createdGroupId).get();
        assertEquals(1, groupWithPostNoSqlOptional.getPosts().size());
    }


    @Test
    void test_updatePost() {
        userCommandService.createUser(TEST_GROUP_CREATOR);
        int creatorId = userRepository.findByEmail("test@test").get().getId();
        NewGroupDto newGroupDto = new NewGroupDto("testGroupName", "testGroupDescription", GroupState.PRIVATE, creatorId);
        groupCommandService.createGroup(newGroupDto);
        int createdGroupId = groupRepository.findByName("testGroupName").get().getId();
        postCommandService.createPost(new NewPostDto(TEST_TITLE, TEST_TEXT, creatorId, createdGroupId));
        List<PostTable> allPosts = findAllPostsAndSortThem();
        int createdPostId = allPosts.get(0).getId();
        UpdatePostDto updatePostDto = new UpdatePostDto(TEST_TITLE + "updated", TEST_TEXT + "updated");


        postCommandService.updatePost(createdPostId, updatePostDto);


        Optional<PostTable> updatedPostOptional = postRepository.findById(createdPostId);
        assertTrue(updatedPostOptional.isPresent());
        PostTable updatedPost = updatedPostOptional.get();
        assertEquals(TEST_TITLE + "updated", updatedPost.getTitle());
        assertEquals(TEST_TEXT + "updated", updatedPost.getText());
        assertEquals(creatorId, updatedPost.getCreator().getId());
        assertEquals(createdGroupId, updatedPost.getGroupReference().getId());

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
        postCommandService.createPost(new NewPostDto(TEST_TITLE, TEST_TEXT, creatorId, createdGroupId));
        int createdPostId = postRepository.findAll().get(0).getId();


        postCommandService.deletePost(createdPostId);


        Optional<PostTable> deletedPostOptional = postRepository.findById(createdPostId);
        assertTrue(deletedPostOptional.isEmpty());

        GroupDocument updatedGroupDocument = groupDocumentRepository.findById(createdGroupId).get();
        assertTrue(updatedGroupDocument.getPosts().isEmpty());
    }

    @Test
    void test_deletePostWithMultiplePostsInGroup() {
        userCommandService.createUser(TEST_GROUP_CREATOR);
        int creatorId = userRepository.findByEmail("test@test").get().getId();
        NewGroupDto newGroupDto = new NewGroupDto("testGroupName", "testGroupDescription", GroupState.PRIVATE, creatorId);
        groupCommandService.createGroup(newGroupDto);
        int createdGroupId = groupRepository.findByName("testGroupName").get().getId();

        NewPostDto firstPost = new NewPostDto(TEST_TITLE, TEST_TEXT, creatorId, createdGroupId);
        postCommandService.createPost(firstPost);

        NewPostDto secondPost = new NewPostDto(TEST_TITLE + "2", TEST_TEXT + "2", creatorId, createdGroupId);
        postCommandService.createPost(secondPost);

        NewPostDto thirdPost = new NewPostDto(TEST_TITLE + "3", TEST_TEXT + "3", creatorId, createdGroupId);
        postCommandService.createPost(thirdPost);

        List<PostTable> allPostsSortedById = findAllPostsAndSortThem();
        int createdPost1Id = allPostsSortedById.get(0).getId();
        int createdPost2Id = allPostsSortedById.get(1).getId();
        int createdPost3Id = allPostsSortedById.get(2).getId();

        postCommandService.deletePost(createdPost2Id);  //delete second post


        Optional<PostTable> firstPostOptional = postRepository.findById(createdPost1Id);
        Optional<PostTable> deletedSecondPostOptional = postRepository.findById(createdPost2Id);
        Optional<PostTable> thirdPostOptional = postRepository.findById(createdPost3Id);

        assertTrue(firstPostOptional.isPresent());
        assertTrue(deletedSecondPostOptional.isEmpty());
        assertTrue(thirdPostOptional.isPresent());


        GroupDocument updatedGroupDocument = groupDocumentRepository.findById(createdGroupId).get();
        assertEquals(2, updatedGroupDocument.getPosts().size());
        assertEquals(createdPost1Id, updatedGroupDocument.getPosts().get(0).getId());
        assertEquals(createdPost3Id, updatedGroupDocument.getPosts().get(1).getId());
    }

    private List<PostTable> findAllPostsAndSortThem() {
        List<PostTable> allPosts = postRepository.findAll();
        allPosts.sort(Comparator.comparingInt(PostTable::getId));
        return allPosts;
    }

}

