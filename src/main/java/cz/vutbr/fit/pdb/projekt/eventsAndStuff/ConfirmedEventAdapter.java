package cz.vutbr.fit.pdb.projekt.eventsAndStuff;

import cz.vutbr.fit.pdb.projekt.features.PersistentUser;

public class ConfirmedEventAdapter<T extends EventInterface> implements EventInterface {

    private final T eventInterface;

    public ConfirmedEventAdapter(T eventInterface) {
        this.eventInterface = eventInterface;
    }

    @Override
    public PersistentUser apply(PersistentUser persistentUser) {
        return eventInterface.apply(persistentUser);
    }

    public T getEventInterface() {
        return eventInterface;
    }

    @Override
    public PersistentUser reverse(PersistentUser persistentBooking) {
        return eventInterface.reverse(persistentBooking);
    }

    @Override
    public String toString() {
        return "ConfirmedEventAdapter for " + eventInterface;
    }
}
