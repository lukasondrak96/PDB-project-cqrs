package cz.vutbr.fit.pdb.projekt.events.events;

import cz.vutbr.fit.pdb.projekt.features.persistent.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentObject;

public class ConfirmedEventAdapter<T extends PersistentObject> extends AbstractEvent<T> implements EventInterface<T> {

    private AbstractEvent<T> event;

    public ConfirmedEventAdapter(ObjectInterface objectInterface, AbstractEvent<T> event) {
        super(objectInterface);
        this.event = event;
        commandService = event.getCommandService();
    }

    @Override
    public T apply(T persistentObject) {
        return event.apply(persistentObject);
    }
}
