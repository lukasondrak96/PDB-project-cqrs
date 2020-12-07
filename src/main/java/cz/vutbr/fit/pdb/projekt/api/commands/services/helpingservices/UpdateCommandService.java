package cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.PersistentObject;

public interface UpdateCommandService<T extends PersistentObject> extends CreateCommandService<T> {

    T finishUpdating(T persistentObject);

}
