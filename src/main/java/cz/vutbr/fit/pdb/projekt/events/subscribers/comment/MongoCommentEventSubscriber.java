package cz.vutbr.fit.pdb.projekt.events.subscribers.comment;

import cz.vutbr.fit.pdb.projekt.events.events.ConfirmedEventAdapter;
import cz.vutbr.fit.pdb.projekt.events.events.comment.CommentDeletedEvent;
import cz.vutbr.fit.pdb.projekt.events.subscribers.AbstractSubscriber;
import cz.vutbr.fit.pdb.projekt.events.subscribers.post.MongoPostEventSubscriber;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentComment;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.CommentEmbedded;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoCommentEventSubscriber extends AbstractSubscriber {

    public static final String RECEIVED_AND_APPLYING_EVENT = "Received and applying {} event {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoPostEventSubscriber.class);
    private static final CommentEmbedded REUSABLE_MONGO_OBJECT = new CommentEmbedded();

    public MongoCommentEventSubscriber(EventBus... eventBuses) {
        super(eventBuses);
    }

    @Subscribe
    public void onCommentCreateConfirmedEvent(ConfirmedEventAdapter<CommentEmbedded> createConfirmedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, createConfirmedEvent.getClass().getSimpleName(), createConfirmedEvent);
        createConfirmedEvent.apply(REUSABLE_MONGO_OBJECT);
    }

    @Subscribe
    public void onCommentDeleteConfirmedEvent(CommentDeletedEvent<PersistentComment> commentDeletedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, commentDeletedEvent.getClass().getSimpleName(), commentDeletedEvent);
        commentDeletedEvent.apply(REUSABLE_MONGO_OBJECT);
    }
}
