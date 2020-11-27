package cz.vutbr.fit.pdb.projekt.events.events;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.PersistentObject;

public class ConfirmedEventAdapter<T extends PersistentObject> extends AbstractEvent<T> implements EventInterface<T> {

    private AbstractEvent<T> event;

    public ConfirmedEventAdapter(AbstractEvent<T> event) {
        super(null);
        this.event = event;
        commandService = event.getCommandService();
    }

    @Override
    public T apply(T persistentObject) {
        return event.apply(persistentObject);
    }
}
