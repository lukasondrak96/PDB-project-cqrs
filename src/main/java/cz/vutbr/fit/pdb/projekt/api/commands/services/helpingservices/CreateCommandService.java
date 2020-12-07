package cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.PersistentObject;

public interface CreateCommandService<T extends PersistentObject> extends CommandService {

    T assignFromTo(ObjectInterface objectInterface, T persistentObject);

    T finishSaving(T persistentObject);

}
