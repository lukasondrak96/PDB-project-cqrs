package cz.vutbr.fit.pdb.projekt.eventsAndStuff.subscribers;

import cz.vutbr.fit.pdb.projekt.eventsAndStuff.events.UserCreatedEvent;
import cz.vutbr.fit.pdb.projekt.features.PersistentUser;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class OracleSourceOfTruthSubscriber extends AbstractSubscriber {

    public OracleSourceOfTruthSubscriber(UserTable user, EventBus... eventBuses) {
        super(eventBuses);
        this.persistentUser = user;
    }

    @Subscribe
    public PersistentUser onConfirmedBookingCreatedEvent(UserCreatedEvent confirmedBookingCreatedEvent) {
        return confirmedBookingCreatedEvent.apply(persistentUser);
    }
}
