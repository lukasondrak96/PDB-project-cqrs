package cz.vutbr.fit.pdb.projekt.api.commands.services;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.PersistentObject;

public interface CommandService<T extends PersistentObject>{

//    public T finishSaving(T object);
//
    T finishUpdating(T persistentObject);
//
//    public T finishDeleting(T object);

    T assignFromTo(ObjectInterface objectInterface, T persistentObject);

    T finishSaving(T persistentObject);
}
