package cz.vutbr.fit.pdb.projekt.events.subscribers.group;

import cz.vutbr.fit.pdb.projekt.events.events.ConfirmedEventAdapter;
import cz.vutbr.fit.pdb.projekt.events.subscribers.AbstractSubscriber;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoGroupEventSubscriber extends AbstractSubscriber {

    public static final String RECEIVED_AND_APPLYING_EVENT = "Received and applying {} event {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoGroupEventSubscriber.class);
    private static final UserDocument REUSABLE_MONGO_OBJECT = new UserDocument();

    public MongoGroupEventSubscriber(EventBus... eventBuses) {
        super(eventBuses);
    }

    @Subscribe
    public void onGroupCreateConfirmedEvent(ConfirmedEventAdapter<UserDocument> createConfirmedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, createConfirmedEvent.getClass().getSimpleName(), createConfirmedEvent);
        createConfirmedEvent.apply(REUSABLE_MONGO_OBJECT);
    }

//    @Subscribe
//    public void onGroupUpdatedEvent(UserUpdatedEvent groupUpdatedEvent) {
//        groupUpdatedEvent.apply(persistentObject);
//    }

}
