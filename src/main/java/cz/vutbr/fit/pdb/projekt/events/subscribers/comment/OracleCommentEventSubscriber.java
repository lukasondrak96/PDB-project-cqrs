package cz.vutbr.fit.pdb.projekt.events.subscribers.comment;

import cz.vutbr.fit.pdb.projekt.events.events.ConfirmedEventAdapter;
import cz.vutbr.fit.pdb.projekt.events.events.OracleCreatedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.comment.CommentDeletedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.comment.CommentUpdatedEvent;
import cz.vutbr.fit.pdb.projekt.events.subscribers.AbstractSubscriber;
import cz.vutbr.fit.pdb.projekt.events.subscribers.group.OracleGroupEventSubscriber;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentComment;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.comment.CommentTable;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OracleCommentEventSubscriber extends AbstractSubscriber {

    public static final String RECEIVED_AND_APPLYING_EVENT = "Received and applying {} event {}";
    public static final String POSTING_EVENT = "Posting {} event {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(OracleGroupEventSubscriber.class);
    private static final CommentTable REUSABLE_ORACLE_OBJECT = new CommentTable();

    public OracleCommentEventSubscriber(EventBus... eventBuses) {
        super(eventBuses);
    }

    @Subscribe
    public void onCommentCreatedEvent(OracleCreatedEvent<PersistentComment> commentCreatedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, commentCreatedEvent.getClass().getSimpleName(), commentCreatedEvent);
        CommentTable commentTable = (CommentTable) commentCreatedEvent.apply(REUSABLE_ORACLE_OBJECT);
        if(commentTable != null) {
            final ConfirmedEventAdapter<PersistentComment> confirmedCommentCreatedEvent = new ConfirmedEventAdapter<>(new OracleCreatedEvent<>(commentTable, commentCreatedEvent.getCommandService()));
            LOGGER.info(POSTING_EVENT, confirmedCommentCreatedEvent.getClass().getSimpleName(), confirmedCommentCreatedEvent);
            post(confirmedCommentCreatedEvent);
        }
    }

    @Subscribe
    public void onCommentUpdatedEvent(CommentUpdatedEvent<PersistentComment> commentUpdatedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, commentUpdatedEvent.getClass().getSimpleName(), commentUpdatedEvent);
        commentUpdatedEvent.apply(REUSABLE_ORACLE_OBJECT);
    }

    @Subscribe
    public void onCommentDeletedEvent(CommentDeletedEvent<PersistentComment> commentDeletedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, commentDeletedEvent.getClass().getSimpleName(), commentDeletedEvent);
        commentDeletedEvent.apply(REUSABLE_ORACLE_OBJECT);
    }
}
