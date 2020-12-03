package cz.vutbr.fit.pdb.projekt.events.subscribers.group;

import cz.vutbr.fit.pdb.projekt.events.events.ConfirmedEventAdapter;
import cz.vutbr.fit.pdb.projekt.events.events.group.GroupAdminChangedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.group.GroupDeletedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.group.GroupStateChangedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.group.GroupUpdatedEvent;
import cz.vutbr.fit.pdb.projekt.events.subscribers.AbstractSubscriber;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentGroup;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocument;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoGroupEventSubscriber extends AbstractSubscriber {

    public static final String RECEIVED_AND_APPLYING_EVENT = "Received and applying {} event {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoGroupEventSubscriber.class);
    private static final GroupDocument REUSABLE_MONGO_OBJECT = new GroupDocument();

    public MongoGroupEventSubscriber(EventBus... eventBuses) {
        super(eventBuses);
    }

    @Subscribe
    public void onGroupCreateConfirmedEvent(ConfirmedEventAdapter<GroupDocument> createConfirmedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, createConfirmedEvent.getClass().getSimpleName(), createConfirmedEvent);
        createConfirmedEvent.apply(REUSABLE_MONGO_OBJECT);
    }

    @Subscribe
    public void onGroupUpdatedEvent(GroupUpdatedEvent<PersistentGroup> groupUpdatedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, groupUpdatedEvent.getClass().getSimpleName(), groupUpdatedEvent);
        groupUpdatedEvent.apply(REUSABLE_MONGO_OBJECT);
    }

    @Subscribe
    public void onGroupDeletedEvent(GroupDeletedEvent<PersistentGroup> groupDeletedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, groupDeletedEvent.getClass().getSimpleName(), groupDeletedEvent);
        groupDeletedEvent.apply(REUSABLE_MONGO_OBJECT);
    }

    @Subscribe
    public void onGroupStateChangedEvent(GroupStateChangedEvent<PersistentGroup> groupStateChangedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, groupStateChangedEvent.getClass().getSimpleName(), groupStateChangedEvent);
        groupStateChangedEvent.apply(REUSABLE_MONGO_OBJECT);
    }

    @Subscribe
    public void onGroupAdminChangedEvent(GroupAdminChangedEvent<PersistentGroup> groupAdminChangedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, groupAdminChangedEvent.getClass().getSimpleName(), groupAdminChangedEvent);
        groupAdminChangedEvent.apply(REUSABLE_MONGO_OBJECT);
    }
}
