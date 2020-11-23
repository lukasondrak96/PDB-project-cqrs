package cz.vutbr.fit.pdb.projekt.events.subscribers;

import cz.vutbr.fit.pdb.projekt.events.events.user.UserCreatedEvent;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentObject;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentUser;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MorphiaReadCacheSubscriber<T extends PersistentObject> extends AbstractSubscriber<T> {

    public MorphiaReadCacheSubscriber(T object, EventBus... eventBuses) {
        super(eventBuses);
        this.persistentObject = object;
    }

    @Subscribe
    public PersistentUser onUserCreatedEvent(UserCreatedEvent userCreatedEvent) {
        return userCreatedEvent.apply((PersistentUser) persistentObject);
    }

}
