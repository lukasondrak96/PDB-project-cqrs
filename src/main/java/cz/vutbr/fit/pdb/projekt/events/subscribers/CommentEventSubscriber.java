package cz.vutbr.fit.pdb.projekt.events.subscribers;

import cz.vutbr.fit.pdb.projekt.events.events.user.UserCreatedEvent;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentComment;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentObject;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentUser;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class CommentEventSubscriber<T extends PersistentComment> extends AbstractSubscriber<T> {

    public CommentEventSubscriber(T comment, EventBus... eventBuses) {
        super(eventBuses);
        this.persistentObject = comment;
    }

    @Subscribe
    public PersistentComment onCommentCreatedEvent(CommentCreatedEvent commentCreatedEvent) {
        return commentCreatedEvent.apply(persistentObject);
    }
}
