package cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.PersistentObject;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupState;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;

public interface GroupChangingService<T extends PersistentObject> extends UpdateCommandService<T> {

    T finishStateChanging(T group, GroupState state);

    T finishAdminChanging(T group, UserTable userTable);

    T finishMemberAdding(T persistentObject, UserTable userTable);

    T finishMemberRemoving(T persistentObject, UserTable userTable);

}
