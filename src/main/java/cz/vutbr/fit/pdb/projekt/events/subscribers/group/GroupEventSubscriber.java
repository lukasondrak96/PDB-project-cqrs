//package cz.vutbr.fit.pdb.projekt.events.subscribers.group;
//
//import cz.vutbr.fit.pdb.projekt.events.events.group.GroupCreatedEvent;
//import cz.vutbr.fit.pdb.projekt.events.events.group.GroupDeletedEvent;
//import cz.vutbr.fit.pdb.projekt.events.events.group.GroupUpdatedEvent;
//import cz.vutbr.fit.pdb.projekt.events.subscribers.AbstractSubscriber;
//import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentGroup;
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//
//public class GroupEventSubscriber<T extends PersistentGroup> extends AbstractSubscriber<T> {
//
//    public GroupEventSubscriber(T group, EventBus... eventBuses) {
//        super(eventBuses);
//        this.persistentObject = group;
//    }
//
//    @Subscribe
//    public PersistentGroup onGroupCreatedEvent(GroupCreatedEvent groupCreatedEvent) {
//        return groupCreatedEvent.apply(persistentObject);
//    }
//
//    @Subscribe
//    public PersistentGroup onGroupUpdatedEvent(GroupUpdatedEvent groupUpdatedEvent) {
//        return groupUpdatedEvent.apply(persistentObject);
//    }
//
//    @Subscribe
//    public PersistentGroup onGroupDeletedEvent(GroupDeletedEvent groupDeletedEvent) {
//        return groupDeletedEvent.apply(persistentObject);
//    }
//}
