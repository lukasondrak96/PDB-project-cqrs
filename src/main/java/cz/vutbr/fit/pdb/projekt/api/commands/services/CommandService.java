package cz.vutbr.fit.pdb.projekt.api.commands.services;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.PersistentObject;

public interface CommandService<T extends PersistentObject>{

//    public T finishSaving(T object);
//
//    public T finishUpdating(T object);
//
//    public T finishDeleting(T object);

    T assignFromTo(ObjectInterface objectInterface, T persistentObject);

    T save(T persistentObject);
}
