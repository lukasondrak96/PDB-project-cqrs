package cz.vutbr.fit.pdb.projekt.events.subscribers;

import cz.vutbr.fit.pdb.projekt.events.events.user.UserCreatedEvent;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentMessage;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentObject;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentUser;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MessageEventSubscriber<T extends PersistentMessage> extends AbstractSubscriber<T> {

    public MessageEventSubscriber(T message, EventBus... eventBuses) {
        super(eventBuses);
        this.persistentObject = message;
    }

    @Subscribe
    public PersistentUser onMessageCreatedEvent(MessageCreatedEvent messageCreatedEvent) {
        return messageCreatedEvent.apply(persistentObject);
    }
}
