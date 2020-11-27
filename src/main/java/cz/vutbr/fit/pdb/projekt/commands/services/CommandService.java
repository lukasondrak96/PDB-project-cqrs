package cz.vutbr.fit.pdb.projekt.commands.services;

import cz.vutbr.fit.pdb.projekt.features.persistent.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentObject;

public interface CommandService<T extends PersistentObject>{

//    public T finishSaving(T object);
//
//    public T finishUpdating(T object);
//
//    public T finishDeleting(T object);

    T assignFromTo(ObjectInterface objectInterface, T persistentObject);

    T save(T persistentObject);
}
