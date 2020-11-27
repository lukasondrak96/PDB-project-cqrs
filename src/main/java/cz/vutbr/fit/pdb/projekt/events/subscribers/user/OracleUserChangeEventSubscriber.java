package cz.vutbr.fit.pdb.projekt.events.subscribers.user;

import cz.vutbr.fit.pdb.projekt.events.events.user.UserActivatedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.user.UserDeactivatedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.user.UserUpdatedEvent;
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
    public void onUserUpdatedEvent(UserUpdatedEvent<PersistentUser> userUpdatedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, userUpdatedEvent.getClass().getSimpleName(), userUpdatedEvent);
        userUpdatedEvent.apply(REUSABLE_ORACLE_OBJECT);
    }

    @Subscribe
    public void onUserActivatedEvent(UserActivatedEvent<PersistentUser> userActivatedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, userActivatedEvent.getClass().getSimpleName(), userActivatedEvent);
        userActivatedEvent.apply(REUSABLE_ORACLE_OBJECT);
    }

    @Subscribe
    public void onUserDeactivatedEvent(UserDeactivatedEvent<PersistentUser> userDeactivatedEvent) {
        LOGGER.info(RECEIVED_AND_APPLYING_EVENT, userDeactivatedEvent.getClass().getSimpleName(), userDeactivatedEvent);
        userDeactivatedEvent.apply(REUSABLE_ORACLE_OBJECT);
    }

}
