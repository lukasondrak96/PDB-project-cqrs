package cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.PersistentObject;

public interface DeleteCommandService<T extends PersistentObject> extends UpdateCommandService<T> {

    T finishDeleting(T persistentObject);

}
