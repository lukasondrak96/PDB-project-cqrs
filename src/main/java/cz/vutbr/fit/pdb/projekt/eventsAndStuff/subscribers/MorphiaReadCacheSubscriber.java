package cz.vutbr.fit.pdb.projekt.eventsAndStuff.subscribers;

import cz.vutbr.fit.pdb.projekt.eventsAndStuff.events.UserCreatedEvent;
import cz.vutbr.fit.pdb.projekt.features.PersistentUser;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MorphiaReadCacheSubscriber extends AbstractSubscriber {
    public MorphiaReadCacheSubscriber(UserDocument user, EventBus... eventBuses) {
        super(eventBuses);
        this.persistentUser = user;
    }

    @Subscribe
    public PersistentUser onConfirmedBookingCreatedEvent(UserCreatedEvent confirmedBookingCreatedEvent) {
        return confirmedBookingCreatedEvent.apply(persistentUser);
    }

}
