package cz.vutbr.fit.pdb.projekt.events.events;

import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentObject;

public interface EventInterface<T extends PersistentObject> {

    T apply(T persistentObject);

    T reverse(T persistentObject);

}
