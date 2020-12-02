package cz.vutbr.fit.pdb.projekt.events.events.group;

import cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices.CommandDeleteService;
import cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices.CommandService;
import cz.vutbr.fit.pdb.projekt.events.events.AbstractEvent;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.PersistentObject;

public class GroupDeletedEvent<T extends PersistentObject> extends AbstractEvent<T> {

    public GroupDeletedEvent(ObjectInterface objectInterface, CommandService<T> commandService) {
        super(objectInterface, commandService);
    }

    @Override
    public T apply(T persistentObject) {
        CommandDeleteService<T> commandService = (CommandDeleteService<T>) getCommandService();
        return commandService.finishDeleting(persistentObject);
    }

}
