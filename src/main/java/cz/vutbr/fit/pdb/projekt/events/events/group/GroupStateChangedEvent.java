package cz.vutbr.fit.pdb.projekt.events.events.group;

import cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices.GroupWithStateChangingService;
import cz.vutbr.fit.pdb.projekt.events.events.AbstractEvent;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.PersistentObject;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupState;

public class GroupStateChangedEvent<T extends PersistentObject> extends AbstractEvent<T> {

    private GroupState groupState;

    public GroupStateChangedEvent(ObjectInterface objectInterface, GroupState groupState, GroupWithStateChangingService<T> commandService) {
        super(objectInterface, commandService);
        this.groupState = groupState;
    }

    @Override
    public T apply(T persistentObject) {
        GroupWithStateChangingService<T> commandService = (GroupWithStateChangingService<T>) getCommandService();
        persistentObject = commandService.assignFromTo(getObjectInterface(), persistentObject);
        return commandService.finishStateChanging(persistentObject, groupState);
    }

}
