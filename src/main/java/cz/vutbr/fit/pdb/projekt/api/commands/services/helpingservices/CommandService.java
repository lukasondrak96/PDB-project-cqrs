package cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.PersistentObject;

public interface CommandService<T extends PersistentObject>{

    T assignFromTo(ObjectInterface objectInterface, T persistentObject);

    T finishSaving(T persistentObject);

    T finishUpdating(T persistentObject);

}
