package cz.vutbr.fit.pdb.projekt.events.subscribers.post;

import cz.vutbr.fit.pdb.projekt.events.events.ConfirmedEventAdapter;
import cz.vutbr.fit.pdb.projekt.events.subscribers.AbstractSubscriber;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.PostEmbedded;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoPostEventSubscriber extends AbstractSubscriber {

    public static final String RECEIVED_AND_APPLYING_EVENT = "Received and applying {} event {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoPostEventSubscriber.class);
    private static final PostEmbedded REUSABLE_MONGO_OBJECT = new PostEmbedded();

    public MongoPostEventSubscriber(EventBus... eventBuses) {
        super(eventBuses);
    }

    @Subscribe
    public void onPostCreateConfirmedEvent(ConfirmedEventAdapter<PostEmbedded> createConfirmedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, createConfirmedEvent.getClass().getSimpleName(), createConfirmedEvent);
        createConfirmedEvent.apply(REUSABLE_MONGO_OBJECT);
    }

//    @Subscribe
//    public void onGroupStateChangedEvent(GroupStateChangedEvent<PersistentGroup> groupStateChangedEvent) {
//        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, groupStateChangedEvent.getClass().getSimpleName(), groupStateChangedEvent);
//        groupStateChangedEvent.apply(REUSABLE_MONGO_OBJECT);
//    }

}
