package cz.vutbr.fit.pdb.projekt.events.subscribers.post;

import cz.vutbr.fit.pdb.projekt.events.events.ConfirmedEventAdapter;
import cz.vutbr.fit.pdb.projekt.events.events.OracleCreatedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.post.PostDeletedEvent;
import cz.vutbr.fit.pdb.projekt.events.subscribers.AbstractSubscriber;
import cz.vutbr.fit.pdb.projekt.events.subscribers.group.OracleGroupEventSubscriber;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentPost;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.post.PostTable;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OraclePostEventSubscriber extends AbstractSubscriber {

    public static final String RECEIVED_AND_APPLYING_EVENT = "Received and applying {} event {}";
    public static final String POSTING_EVENT = "Posting {} event {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(OracleGroupEventSubscriber.class);
    private static final PostTable REUSABLE_ORACLE_OBJECT = new PostTable();

    public OraclePostEventSubscriber(EventBus... eventBuses) {
        super(eventBuses);
    }

    @Subscribe
    public void onPostCreatedEvent(OracleCreatedEvent<PersistentPost> postCreatedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, postCreatedEvent.getClass().getSimpleName(), postCreatedEvent);
        PostTable postTable = (PostTable) postCreatedEvent.apply(REUSABLE_ORACLE_OBJECT);
        if(postTable != null) {
            final ConfirmedEventAdapter<PersistentPost> confirmedPostCreatedEvent = new ConfirmedEventAdapter<>(new OracleCreatedEvent<>(postTable, postCreatedEvent.getCommandService()));
            LOGGER.info(POSTING_EVENT, confirmedPostCreatedEvent.getClass().getSimpleName(), confirmedPostCreatedEvent);
            post(confirmedPostCreatedEvent);
        }
    }

    @Subscribe
    public void onPostDeletedEvent(PostDeletedEvent<PersistentPost> postDeletedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, postDeletedEvent.getClass().getSimpleName(), postDeletedEvent);
        postDeletedEvent.apply(REUSABLE_ORACLE_OBJECT);
    }
}
