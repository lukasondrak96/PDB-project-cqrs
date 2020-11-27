package cz.vutbr.fit.pdb.projekt.events.subscribers.user;

import cz.vutbr.fit.pdb.projekt.events.events.ConfirmedEventAdapter;
import cz.vutbr.fit.pdb.projekt.events.subscribers.AbstractSubscriber;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentUser;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OracleUserChangeEventSubscriber extends AbstractSubscriber {

    public static final String RECEIVED_AND_APPLYING_EVENT = "Received and applying {} event {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(OracleUserCreateEventSubscriber.class);
    private static final UserTable REUSABLE_ORACLE_OBJECT = new UserTable();

    public OracleUserChangeEventSubscriber(EventBus... eventBuses) {
        super(eventBuses);
    }

    @Subscribe
    public void onUserUpdatedEvent(ConfirmedEventAdapter<PersistentUser> confirmedEventAdapter) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, confirmedEventAdapter.getClass().getSimpleName(), confirmedEventAdapter);
        confirmedEventAdapter.apply(REUSABLE_ORACLE_OBJECT);
    }

}
