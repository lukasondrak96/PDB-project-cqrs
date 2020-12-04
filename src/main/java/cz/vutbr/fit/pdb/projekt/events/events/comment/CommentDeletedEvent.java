package cz.vutbr.fit.pdb.projekt.events.events.comment;

import cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices.CommandDeleteService;
import cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices.CommandService;
import cz.vutbr.fit.pdb.projekt.events.events.AbstractEvent;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.PersistentObject;

public class CommentDeletedEvent<T extends PersistentObject> extends AbstractEvent<T> {

    public CommentDeletedEvent(ObjectInterface objectInterface, CommandService<T> commandService) {
        super(objectInterface, commandService);
    }

    @Override
    public T apply(T persistentObject) {
        CommandDeleteService<T> commandService = (CommandDeleteService<T>) getCommandService();
        persistentObject = commandService.assignFromTo(getObjectInterface(), persistentObject);
        return commandService.finishDeleting(persistentObject);
    }

}
