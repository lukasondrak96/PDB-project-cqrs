package cz.vutbr.fit.pdb.projekt.events.events.group;

import cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices.CommandService;
import cz.vutbr.fit.pdb.projekt.events.events.AbstractEvent;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.PersistentObject;

public class GroupUpdatedEvent<T extends PersistentObject> extends AbstractEvent<T> {

    public GroupUpdatedEvent(ObjectInterface objectInterface, CommandService<T> commandService) {
        super(objectInterface, commandService);
    }

    @Override
    public T apply(T persistentObject) {
        CommandService<T> commandService = getCommandService();
        persistentObject = commandService.assignFromTo(getObjectInterface(), persistentObject);
        return commandService.finishUpdating(persistentObject);
    }

}
