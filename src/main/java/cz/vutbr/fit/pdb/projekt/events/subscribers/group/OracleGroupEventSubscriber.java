package cz.vutbr.fit.pdb.projekt.events.subscribers.group;

import cz.vutbr.fit.pdb.projekt.events.events.ConfirmedEventAdapter;
import cz.vutbr.fit.pdb.projekt.events.events.OracleCreatedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.group.*;
import cz.vutbr.fit.pdb.projekt.events.subscribers.AbstractSubscriber;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentGroup;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupTable;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OracleGroupEventSubscriber extends AbstractSubscriber {

    public static final String RECEIVED_AND_APPLYING_EVENT = "Received and applying {} event {}";
    public static final String POSTING_EVENT = "Posting {} event {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(OracleGroupEventSubscriber.class);
    private static final GroupTable REUSABLE_ORACLE_OBJECT = new GroupTable();

    public OracleGroupEventSubscriber(EventBus... eventBuses) {
        super(eventBuses);
    }

    @Subscribe
    public void onGroupCreatedEvent(OracleCreatedEvent<PersistentGroup> groupCreatedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, groupCreatedEvent.getClass().getSimpleName(), groupCreatedEvent);
        GroupTable groupTable = (GroupTable) groupCreatedEvent.apply(REUSABLE_ORACLE_OBJECT);
        if(groupTable != null) {
            final ConfirmedEventAdapter<PersistentGroup> confirmedGroupCreatedEvent = new ConfirmedEventAdapter<>(new OracleCreatedEvent<>(groupTable, groupCreatedEvent.getCommandService()));
            LOGGER.info(POSTING_EVENT, confirmedGroupCreatedEvent.getClass().getSimpleName(), confirmedGroupCreatedEvent);
            post(confirmedGroupCreatedEvent);
        }
    }

    @Subscribe
    public void onGroupUpdatedEvent(GroupUpdatedEvent<PersistentGroup> groupUpdatedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, groupUpdatedEvent.getClass().getSimpleName(), groupUpdatedEvent);
        groupUpdatedEvent.apply(REUSABLE_ORACLE_OBJECT);
    }

    @Subscribe
    public void onGroupDeletedEvent(GroupDeletedEvent<PersistentGroup> groupDeletedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, groupDeletedEvent.getClass().getSimpleName(), groupDeletedEvent);
        groupDeletedEvent.apply(REUSABLE_ORACLE_OBJECT);
    }

    @Subscribe
    public void onGroupStateChangedEvent(GroupStateChangedEvent<PersistentGroup> groupStateChangedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, groupStateChangedEvent.getClass().getSimpleName(), groupStateChangedEvent);
        groupStateChangedEvent.apply(REUSABLE_ORACLE_OBJECT);
    }

    @Subscribe
    public void onGroupAdminChangedEvent(GroupAdminChangedEvent<PersistentGroup> groupAdminChangedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, groupAdminChangedEvent.getClass().getSimpleName(), groupAdminChangedEvent);
        groupAdminChangedEvent.apply(REUSABLE_ORACLE_OBJECT);
    }

    @Subscribe
    public void onGroupMemberAddedEvent(GroupMemberAddedEvent<PersistentGroup> groupMemberAddedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, groupMemberAddedEvent.getClass().getSimpleName(), groupMemberAddedEvent);
        groupMemberAddedEvent.apply(REUSABLE_ORACLE_OBJECT);
    }
}
