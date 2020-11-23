package cz.vutbr.fit.pdb.projekt.events.subscribers;

import cz.vutbr.fit.pdb.projekt.events.events.user.UserCreatedEvent;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentObject;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentPost;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentUser;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class PostEventSubscriber<T extends PersistentPost> extends AbstractSubscriber<T> {

    public PostEventSubscriber(T post, EventBus... eventBuses) {
        super(eventBuses);
        this.persistentObject = post;
    }

    @Subscribe
    public PersistentPost onUserCreatedEvent(PostCreatedEvent postCreatedEvent) {
        return postCreatedEvent.apply(persistentObject);
    }
}
