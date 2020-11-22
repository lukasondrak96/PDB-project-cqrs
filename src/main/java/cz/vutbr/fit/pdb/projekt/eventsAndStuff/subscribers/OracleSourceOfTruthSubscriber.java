package cz.vutbr.fit.pdb.projekt.eventsAndStuff.subscribers;

import cz.vutbr.fit.pdb.projekt.eventsAndStuff.ConfirmedEventAdapter;
import cz.vutbr.fit.pdb.projekt.eventsAndStuff.events.UserCreatedEvent;
import cz.vutbr.fit.pdb.projekt.features.PersistentUser;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class OracleSourceOfTruthSubscriber extends AbstractSubscriber {

    private static final UserTable EMPTY_USER_DOCUMENT = new UserTable();


    public OracleSourceOfTruthSubscriber(EventBus... eventBuses) {
        super(eventBuses);
    }

    @Subscribe
    public PersistentUser onConfirmedBookingCreatedEvent(ConfirmedEventAdapter<UserCreatedEvent> confirmedBookingCreatedEvent) {
        return confirmedBookingCreatedEvent.apply(EMPTY_USER_DOCUMENT);
    }
}
