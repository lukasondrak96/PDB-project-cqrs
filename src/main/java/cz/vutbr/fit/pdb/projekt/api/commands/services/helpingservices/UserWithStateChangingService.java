package cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.PersistentObject;

public interface UserWithStateChangingService<T extends PersistentObject> extends CommandService<T> {

    T finishActivating(T object);

    T finishDeactivating(T object);

}
