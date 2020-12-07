package cz.vutbr.fit.pdb.projekt.events.subscribers.message;

import cz.vutbr.fit.pdb.projekt.events.events.ConfirmedEventAdapter;
import cz.vutbr.fit.pdb.projekt.events.events.OracleCreatedEvent;
import cz.vutbr.fit.pdb.projekt.events.subscribers.AbstractSubscriber;
import cz.vutbr.fit.pdb.projekt.events.subscribers.group.OracleGroupEventSubscriber;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentMessage;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.message.MessageTable;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OracleMessageEventSubscriber extends AbstractSubscriber {

    public static final String RECEIVED_AND_APPLYING_EVENT = "Received and applying {} event {}";
    public static final String POSTING_EVENT = "Posting {} event {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(OracleGroupEventSubscriber.class);
    private static final MessageTable REUSABLE_ORACLE_OBJECT = new MessageTable();

    public OracleMessageEventSubscriber(EventBus... eventBuses) {
        super(eventBuses);
    }

    @Subscribe
    public void onMessageCreatedEvent(OracleCreatedEvent<PersistentMessage> messageCreatedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, messageCreatedEvent.getClass().getSimpleName(), messageCreatedEvent);
        MessageTable messageTable = (MessageTable) messageCreatedEvent.apply(REUSABLE_ORACLE_OBJECT);
        if(messageTable != null) {
            final ConfirmedEventAdapter<PersistentMessage> confirmedMessageCreatedEvent = new ConfirmedEventAdapter<>(new OracleCreatedEvent<>(messageTable, messageCreatedEvent.getCommandService()));
            LOGGER.info(POSTING_EVENT, confirmedMessageCreatedEvent.getClass().getSimpleName(), confirmedMessageCreatedEvent);
            post(confirmedMessageCreatedEvent);
        }
    }
}
