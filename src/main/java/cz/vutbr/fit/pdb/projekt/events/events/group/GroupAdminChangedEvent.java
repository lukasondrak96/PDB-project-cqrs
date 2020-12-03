package cz.vutbr.fit.pdb.projekt.events.events.group;

import cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices.GroupChangingService;
import cz.vutbr.fit.pdb.projekt.events.events.AbstractEvent;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.PersistentObject;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;

public class GroupAdminChangedEvent<T extends PersistentObject> extends AbstractEvent<T> {

    private UserTable userTable;

    public GroupAdminChangedEvent(ObjectInterface objectInterface, UserTable userTable, GroupChangingService<T> commandService) {
        super(objectInterface, commandService);
        this.userTable = userTable;
    }

    @Override
    public T apply(T persistentObject) {
        GroupChangingService<T> commandService = (GroupChangingService<T>) getCommandService();
        persistentObject = commandService.assignFromTo(getObjectInterface(), persistentObject);
        return commandService.finishAdminChanging(persistentObject, userTable);
    }

}
