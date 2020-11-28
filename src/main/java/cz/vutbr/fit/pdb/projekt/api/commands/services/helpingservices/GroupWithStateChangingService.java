package cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.PersistentObject;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupState;

public interface GroupWithStateChangingService<T extends PersistentObject> extends CommandService<T> {
    T finishStateChanging(T group, GroupState state);
}
