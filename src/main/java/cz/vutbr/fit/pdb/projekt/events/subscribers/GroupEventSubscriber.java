package cz.vutbr.fit.pdb.projekt.events.subscribers;

import cz.vutbr.fit.pdb.projekt.events.events.user.UserCreatedEvent;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentGroup;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentObject;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentUser;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class GroupEventSubscriber<T extends PersistentGroup> extends AbstractSubscriber<T> {

    public GroupEventSubscriber(T group, EventBus... eventBuses) {
        super(eventBuses);
        this.persistentObject = group;
    }

    @Subscribe
    public PersistentGroup onGroupCreatedEvent(GroupCreatedEvent groupCreatedEvent) {
        return groupCreatedEvent.apply(persistentObject);
    }
}
