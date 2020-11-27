//package cz.vutbr.fit.pdb.projekt.events.subscribers.comment;
//
//import cz.vutbr.fit.pdb.projekt.events.events.comment.CommentCreatedEvent;
//import cz.vutbr.fit.pdb.projekt.events.events.comment.CommentDeletedEvent;
//import cz.vutbr.fit.pdb.projekt.events.events.comment.CommentUpdatedEvent;
//import cz.vutbr.fit.pdb.projekt.events.subscribers.AbstractSubscriber;
//import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentComment;
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//
//public class CommentEventSubscriber<T extends PersistentComment> extends AbstractSubscriber<T> {
//
//    public CommentEventSubscriber(T comment, EventBus... eventBuses) {
//        super(eventBuses);
//        this.persistentObject = comment;
//    }
//
//    @Subscribe
//    public PersistentComment onCommentCreatedEvent(CommentCreatedEvent commentCreatedEvent) {
//        return commentCreatedEvent.apply(persistentObject);
//    }
//
//    @Subscribe
//    public PersistentComment onCommentUpdatedEvent(CommentUpdatedEvent commentUpdatedEvent) {
//        return commentUpdatedEvent.apply(persistentObject);
//    }
//
//    @Subscribe
//    public PersistentComment onCommentDeletedEvent(CommentDeletedEvent commentDeletedEvent) {
//        return commentDeletedEvent.apply(persistentObject);
//    }
//}
