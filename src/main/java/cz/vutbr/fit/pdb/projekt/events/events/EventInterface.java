package cz.vutbr.fit.pdb.projekt.events.events;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.PersistentObject;

public interface EventInterface<T extends PersistentObject> {

    T apply(T persistentObject);
}
