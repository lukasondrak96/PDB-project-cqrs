package cz.vutbr.fit.pdb.projekt.api.commands.services;

import cz.vutbr.fit.pdb.projekt.api.commands.dtos.post.NewPostDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.post.UpdatePostDto;
import cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices.DeleteCommandService;
import cz.vutbr.fit.pdb.projekt.events.events.AbstractEvent;
import cz.vutbr.fit.pdb.projekt.events.events.OracleCreatedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.post.PostDeletedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.post.PostUpdatedEvent;
import cz.vutbr.fit.pdb.projekt.events.subscribers.post.MongoPostEventSubscriber;
import cz.vutbr.fit.pdb.projekt.events.subscribers.post.OraclePostEventSubscriber;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.objects.PostInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentPost;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.PostEmbedded;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupTable;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.post.PostRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.post.PostTable;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import lombok.AllArgsConstructor;
import org.greenrobot.eventbus.EventBus;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
@AllArgsConstructor
public class PostCommandService implements DeleteCommandService<PersistentPost> {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final MongoTemplate mongoTemplate;

    private static final EventBus EVENT_BUS = EventBus.getDefault();

    public ResponseEntity<?> createPost(NewPostDto newPostDto) {
        Optional<UserTable> userOptional = userRepository.findById(newPostDto.getCreatorId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uživatel s tímto id neexistuje");
        }
        UserTable creator = userOptional.get();

        Optional<GroupTable> groupOptional = groupRepository.findById(newPostDto.getGroupId());
        if (groupOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Skupina s tímto id neexistuje");
        }
        GroupTable group = groupOptional.get();

        final PostTable postTable = new PostTable(newPostDto.getTitle(), newPostDto.getText(), new Date(), group, creator);
        OracleCreatedEvent<PersistentPost> oracleCreatedEvent = new OracleCreatedEvent<>(postTable, this);
        subscribeEventToOracleAndMongo(oracleCreatedEvent);
        return ResponseEntity.ok().body("Příspěvek byl vytvořen");
    }


    public ResponseEntity<?> updatePost(int postId, UpdatePostDto updatePostDto) {
        Optional<PostTable> postTableOptional = postRepository.findById(postId);
        if (postTableOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Příspěvek s tímto id neexistuje");

        PostTable oldPostTable = postTableOptional.get();

        if (isPostTableEqualToUpdatePostDto(oldPostTable, updatePostDto)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nebyly nalezeny žádné změny pro editaci");
        }

        final PostTable postTable = new PostTable(postId, updatePostDto.getTitle(), updatePostDto.getText(),
                oldPostTable.getCreatedAt(), oldPostTable.getGroupReference(), oldPostTable.getCreator());

        PostUpdatedEvent<PersistentPost> updatedEvent = new PostUpdatedEvent<>(postTable, this);
        subscribeEventToOracleAndMongo(updatedEvent);

        return ResponseEntity.ok().body("Příspěvek byl aktualizován");
    }

    public ResponseEntity<?> deletePost(int postId) {
        Optional<PostTable> postTableOptional = postRepository.findById(postId);
        if (postTableOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Příspěvek s tímto id neexistuje");

        PostTable postTable = postTableOptional.get();

        PostDeletedEvent<PersistentPost> deletedEvent = new PostDeletedEvent<>(postTable, this);
        subscribeEventToOracleAndMongo(deletedEvent);

        return ResponseEntity.ok().body("Příspěvek byl smazán");
    }

    /* methods called from events */
    @Override
    public PersistentPost assignFromTo(ObjectInterface objectInterface, PersistentPost post) {
        PostInterface persistentPostInterface = (PostInterface) post;
        PostInterface postInterface = (PostInterface) objectInterface;
        if (post instanceof PostTable || post instanceof PostEmbedded) {
            persistentPostInterface.setId(postInterface.getId());
            persistentPostInterface.setTitle(postInterface.getTitle());
            persistentPostInterface.setText(postInterface.getText());
            persistentPostInterface.setCreatedAt(postInterface.getCreatedAt());
            persistentPostInterface.setGroupReference(postInterface.getGroupReference());
            persistentPostInterface.setCreator(postInterface.getCreator());
        }
        return (PersistentPost) persistentPostInterface;
    }

    @Override
    public PersistentPost finishSaving(PersistentPost post) {
        if (post instanceof PostTable) {
            return postRepository.save((PostTable) post);
        } else {
            addPostToGroup((PostEmbedded) post);
            return post;
        }
    }

    @Override
    public PersistentPost finishUpdating(PersistentPost post) {
        if (post instanceof PostTable) {
            return postRepository.save((PostTable) post);
        } else {
            updatePostInGroup((PostEmbedded) post);
            return post;
        }
    }

    @Override
    public PersistentPost finishDeleting(PersistentPost post) {
        if (post instanceof PostTable)
            postRepository.deleteById(((PostTable) post).getId());
        else
            removePostFromGroup((PostEmbedded) post);
        return null;
    }

    /* private methods */
    private boolean isPostTableEqualToUpdatePostDto(PostTable table, UpdatePostDto dto) {
        return dto.getText().equals(table.getText()) &&
                dto.getTitle().equals(table.getTitle());
    }

    private void subscribeEventToOracleAndMongo(AbstractEvent<PersistentPost> event) {
        OraclePostEventSubscriber sqlSubscriber = new OraclePostEventSubscriber(EVENT_BUS);
        MongoPostEventSubscriber noSqlSubscriber = new MongoPostEventSubscriber(EVENT_BUS);
        EVENT_BUS.post(event);

        EVENT_BUS.unregister(sqlSubscriber);
        EVENT_BUS.unregister(noSqlSubscriber);
    }

    private void addPostToGroup(PostEmbedded post) {
        mongoTemplate.updateMulti(
                new Query(where("id").is(post.getGroupReference().getId())),
                new Update()
                        .push("posts", post),
                GroupDocument.class
        );
    }

    private void updatePostInGroup(PostEmbedded post) {
        mongoTemplate.updateMulti(
                new Query(where("posts.id").is(post.getGroupReference().getId())),
                new Update()
                        .set("posts.$", post),
                GroupDocument.class
        );
    }

    private void removePostFromGroup(PostEmbedded post) {
        mongoTemplate.updateMulti(
                new Query(where("id").is(post.getGroupReference().getId())),
                new Update()
                        .pull("posts", post),
                GroupDocument.class
        );
    }
}
