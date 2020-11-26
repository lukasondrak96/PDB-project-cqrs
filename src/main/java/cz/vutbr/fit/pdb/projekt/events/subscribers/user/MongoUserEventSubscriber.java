package cz.vutbr.fit.pdb.projekt.events.subscribers.user;

import cz.vutbr.fit.pdb.projekt.events.events.user.UserCreatedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.user.UserUpdatedEvent;
import cz.vutbr.fit.pdb.projekt.events.subscribers.AbstractSubscriber;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MongoUserEventSubscriber<T extends UserDocument> extends AbstractSubscriber<T> {

    public MongoUserEventSubscriber(T user, EventBus... eventBuses) {
        super(eventBuses);
        this.persistentObject = user;
    }

    @Subscribe
    public void onUserCreatedEvent(UserCreatedEvent userCreatedEvent) {
        userCreatedEvent.apply(persistentObject);
    }

    @Subscribe
    public void onUserUpdatedEvent(UserUpdatedEvent userUpdatedEvent) {
        userUpdatedEvent.apply(persistentObject);
    }

}
