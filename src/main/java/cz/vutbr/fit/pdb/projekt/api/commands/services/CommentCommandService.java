package cz.vutbr.fit.pdb.projekt.api.commands.services;

import cz.vutbr.fit.pdb.projekt.api.commands.dtos.comment.NewCommentDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.comment.UpdateCommentDto;
import cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices.DeleteCommandService;
import cz.vutbr.fit.pdb.projekt.events.events.AbstractEvent;
import cz.vutbr.fit.pdb.projekt.events.events.OracleCreatedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.comment.CommentDeletedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.comment.CommentUpdatedEvent;
import cz.vutbr.fit.pdb.projekt.events.subscribers.comment.MongoCommentEventSubscriber;
import cz.vutbr.fit.pdb.projekt.events.subscribers.comment.OracleCommentEventSubscriber;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.objects.CommentInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentComment;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.CommentEmbedded;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.comment.CommentRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.comment.CommentTable;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupState;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.post.PostRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.post.PostTable;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserState;
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
public class CommentCommandService implements DeleteCommandService<PersistentComment> {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    private static final EventBus EVENT_BUS = EventBus.getDefault();

    public ResponseEntity<?> createComment(NewCommentDto newCommentDto) {
        Optional<UserTable> userOptional = userRepository.findById(newCommentDto.getCreatorId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("U??ivatel s t??mto id neexistuje");
        }
        UserTable creator = userOptional.get();

        if (creator.getState() == UserState.DEACTIVATED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("U??ivatel m?? deaktivovan?? ????et, nelze vytvo??it nov?? koment????.");
        }

        Optional<PostTable> postOptional = postRepository.findById(newCommentDto.getPostId());
        if (postOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("P????sp??vek s t??mto id neexistuje");
        }
        PostTable post = postOptional.get();

        if (post.getGroupReference().getState() == GroupState.ARCHIVED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Skupina, ve kter?? se nach??z?? p????sp??vek, je archivovan??, nelze k n??mu p??id??vat koment????e");
        }

        if (creator.getGroups().stream().noneMatch(groupTable -> groupTable.getId() == post.getGroupReference().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("U??ivatel nen?? ??lenem skupiny, ve kter?? se p????sp??vek nach??z??, nem????e k n??mu ps??t koment????e");
        }

        final CommentTable commentTable = new CommentTable(newCommentDto.getText(), new Date(), post, creator);
        OracleCreatedEvent<PersistentComment> oracleCreatedEvent = new OracleCreatedEvent<>(commentTable, this);
        subscribeEventToOracleAndMongo(oracleCreatedEvent);
        return ResponseEntity.ok().body("Koment???? byl vytvo??en");
    }


    public ResponseEntity<?> updateComment(int commentId, UpdateCommentDto updateCommentDto) {
        Optional<CommentTable> commentTableOptional = commentRepository.findById(commentId);
        if (commentTableOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Koment???? s t??mto id neexistuje");

        CommentTable oldCommentTable = commentTableOptional.get();

        if (isCommentTableEqualToUpdateCommentDto(oldCommentTable, updateCommentDto)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nebyly nalezeny ????dn?? zm??ny pro editaci");
        }

        final CommentTable commentTable = new CommentTable(commentId, updateCommentDto.getText(),
                oldCommentTable.getCreatedAt(), oldCommentTable.getPostReference(), oldCommentTable.getCreator());

        CommentUpdatedEvent<PersistentComment> updatedEvent = new CommentUpdatedEvent<>(commentTable, this);
        subscribeEventToOracleAndMongo(updatedEvent);

        return ResponseEntity.ok().body("Koment???? byl aktualizov??n");
    }

    public ResponseEntity<?> deleteComment(int commentId) {
        Optional<CommentTable> commentTableOptional = commentRepository.findById(commentId);
        if (commentTableOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Koment???? s t??mto id neexistuje");

        CommentTable commentTable = commentTableOptional.get();

        CommentDeletedEvent<PersistentComment> deletedEvent = new CommentDeletedEvent<>(commentTable, this);
        subscribeEventToOracleAndMongo(deletedEvent);

        return ResponseEntity.ok().body("Koment???? byl smaz??n");
    }

    @Override
    public PersistentComment assignFromTo(ObjectInterface objectInterface, PersistentComment comment) {
        CommentInterface persistentCommandInterface = (CommentInterface) comment;
        CommentInterface commentInterface = (CommentInterface) objectInterface;
        if (comment instanceof CommentTable || comment instanceof CommentEmbedded) {
            persistentCommandInterface.setId(commentInterface.getId());
            persistentCommandInterface.setText(commentInterface.getText());
            persistentCommandInterface.setCreatedAt(commentInterface.getCreatedAt());
            persistentCommandInterface.setPostReference(commentInterface.getPostReference());
            persistentCommandInterface.setCreator(commentInterface.getCreator());
        }
        return (PersistentComment) persistentCommandInterface;
    }

    /* methods called from events */
    @Override
    public PersistentComment finishSaving(PersistentComment comment) {
        if (comment instanceof CommentTable) {
            return commentRepository.save((CommentTable) comment);
        } else {
            addCommentToPost((CommentEmbedded) comment);
            return comment;
        }
    }

    @Override
    public PersistentComment finishUpdating(PersistentComment comment) {
        if (comment instanceof CommentTable) {
            return commentRepository.save((CommentTable) comment);
        } else {
            updateCommentInPost((CommentEmbedded) comment);
            return comment;
        }
    }

    @Override
    public PersistentComment finishDeleting(PersistentComment comment) {
        if (comment instanceof CommentTable)
            commentRepository.delete((CommentTable) comment);
        else
            removeCommentFromPost((CommentEmbedded) comment);
        return null;
    }

    /* private methods */
    private boolean isCommentTableEqualToUpdateCommentDto(CommentTable table, UpdateCommentDto dto) {
        return dto.getText().equals(table.getText());
    }

    private void subscribeEventToOracleAndMongo(AbstractEvent<PersistentComment> event) {
        OracleCommentEventSubscriber sqlSubscriber = new OracleCommentEventSubscriber(EVENT_BUS);
        MongoCommentEventSubscriber noSqlSubscriber = new MongoCommentEventSubscriber(EVENT_BUS);

        EVENT_BUS.post(event);
        EVENT_BUS.unregister(sqlSubscriber);
        EVENT_BUS.unregister(noSqlSubscriber);
    }

    private void addCommentToPost(CommentEmbedded comment) {
        mongoTemplate.updateMulti(
                new Query(where("posts.id").is(comment.getPostReference().getId())),
                new Update()
                        .push("posts.$.comments", comment),
                GroupDocument.class
        );
    }

    private void updateCommentInPost(CommentEmbedded comment) {
        mongoTemplate.updateMulti(
                new Query(where("posts.comments.id").is(comment.getId())),
                new Update()
                        .set("posts.$[outer].comments.$[inner]", comment),
                GroupDocument.class
        );
    }

    private void removeCommentFromPost(CommentEmbedded comment) {
        mongoTemplate.updateMulti(
                new Query(where("posts.id").is(comment.getPostReference().getId())),
                new Update()
                        .pull("posts.$.comments", comment),
                GroupDocument.class
        );
    }
}
