package cz.vutbr.fit.pdb.projekt.events.subscribers;

import cz.vutbr.fit.pdb.projekt.events.events.user.UserCreatedEvent;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentObject;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentUser;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class UserEventSubscriber<T extends PersistentUser> extends AbstractSubscriber<T> {

    public UserEventSubscriber(T user, EventBus... eventBuses) {
        super(eventBuses);
        this.persistentObject = user;
    }

    @Subscribe
    public PersistentUser onUserCreatedEvent(UserCreatedEvent userCreatedEvent) {
        return userCreatedEvent.apply(persistentObject);
    }
}
