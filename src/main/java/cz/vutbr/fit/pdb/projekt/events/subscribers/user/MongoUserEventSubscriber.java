package cz.vutbr.fit.pdb.projekt.events.subscribers.user;

import cz.vutbr.fit.pdb.projekt.events.events.ConfirmedEventAdapter;
import cz.vutbr.fit.pdb.projekt.events.events.user.UserUpdatedEvent;
import cz.vutbr.fit.pdb.projekt.events.subscribers.AbstractSubscriber;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentUser;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoUserEventSubscriber extends AbstractSubscriber {

    public static final String RECEIVED_AND_APPLYING_EVENT = "Received and applying {} event {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoUserEventSubscriber.class);
    private static final UserDocument REUSABLE_MONGO_OBJECT = new UserDocument();

    public MongoUserEventSubscriber(EventBus... eventBuses) {
        super(eventBuses);
    }

    @Subscribe
    public void onUserCreateConfirmedEvent(ConfirmedEventAdapter<UserDocument> createConfirmedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, createConfirmedEvent.getClass().getSimpleName(), createConfirmedEvent);
        createConfirmedEvent.apply(REUSABLE_MONGO_OBJECT);
    }

    @Subscribe
    public void onUserUpdatedEvent(UserUpdatedEvent<PersistentUser> userUpdatedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, userUpdatedEvent.getClass().getSimpleName(), userUpdatedEvent);
        userUpdatedEvent.apply(REUSABLE_MONGO_OBJECT);
    }

}
