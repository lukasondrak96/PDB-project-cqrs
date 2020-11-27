package cz.vutbr.fit.pdb.projekt.events.subscribers.user;

import cz.vutbr.fit.pdb.projekt.events.events.ConfirmedEventAdapter;
import cz.vutbr.fit.pdb.projekt.events.events.OracleCreatedEvent;
import cz.vutbr.fit.pdb.projekt.events.subscribers.AbstractSubscriber;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentUser;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OracleUserCreateEventSubscriber extends AbstractSubscriber {

    public static final String RECEIVED_AND_APPLYING_EVENT = "Received and applying {} event {}";
    public static final String POSTING_EVENT = "Posting {} event {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(OracleUserCreateEventSubscriber.class);
    private static final UserTable REUSABLE_ORACLE_OBJECT = new UserTable();

    public OracleUserCreateEventSubscriber(EventBus... eventBuses) {
        super(eventBuses);
    }

    @Subscribe
    public void onUserCreatedEvent(OracleCreatedEvent<PersistentUser> userCreatedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, userCreatedEvent.getClass().getSimpleName(), userCreatedEvent);
        UserTable userTable = (UserTable) userCreatedEvent.apply(REUSABLE_ORACLE_OBJECT);
        if(userTable != null) {
            final ConfirmedEventAdapter<PersistentUser> confirmedUserCreatedEvent = new ConfirmedEventAdapter<>(new OracleCreatedEvent<>(userTable, userCreatedEvent.getCommandService()));
            LOGGER.info(POSTING_EVENT, confirmedUserCreatedEvent.getClass().getSimpleName(), confirmedUserCreatedEvent);
            post(confirmedUserCreatedEvent);
        }
    }

}
