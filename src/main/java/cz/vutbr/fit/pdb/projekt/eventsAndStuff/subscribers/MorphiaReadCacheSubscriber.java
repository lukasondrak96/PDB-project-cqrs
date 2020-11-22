package cz.vutbr.fit.pdb.projekt.eventsAndStuff.subscribers;

import cz.vutbr.fit.pdb.projekt.eventsAndStuff.ConfirmedEventAdapter;
import cz.vutbr.fit.pdb.projekt.eventsAndStuff.events.UserCreatedEvent;
import cz.vutbr.fit.pdb.projekt.features.PersistentUser;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MorphiaReadCacheSubscriber extends AbstractSubscriber {

    private static final UserDocument EMPTY_USER_DOCUMENT = new UserDocument();

    public MorphiaReadCacheSubscriber(EventBus... eventBuses) {
        super(eventBuses);
    }

    @Subscribe
    public PersistentUser onConfirmedBookingCreatedEvent(ConfirmedEventAdapter<UserCreatedEvent> confirmedUserCreatedEvent) {
        return confirmedUserCreatedEvent.apply(EMPTY_USER_DOCUMENT);
    }

}
