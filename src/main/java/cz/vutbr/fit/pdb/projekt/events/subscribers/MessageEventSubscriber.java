package cz.vutbr.fit.pdb.projekt.events.subscribers;

import cz.vutbr.fit.pdb.projekt.events.events.message.MessageCreatedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.message.MessageDeletedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.message.MessageUpdatedEvent;
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
    public PersistentMessage onMessageCreatedEvent(MessageCreatedEvent messageCreatedEvent) {
        return messageCreatedEvent.apply(persistentObject);
    }

    @Subscribe
    public PersistentMessage onMessageUpdatedEvent(MessageUpdatedEvent messageUpdatedEvent) {
        return messageUpdatedEvent.apply(persistentObject);
    }

    @Subscribe
    public PersistentMessage onMessageDeletedEvent(MessageDeletedEvent messageDeletedEvent) {
        return messageDeletedEvent.apply(persistentObject);
    }
}
