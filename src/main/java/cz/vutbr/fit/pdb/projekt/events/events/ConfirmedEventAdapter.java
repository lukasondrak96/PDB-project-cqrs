package cz.vutbr.fit.pdb.projekt.events.events;

import cz.vutbr.fit.pdb.projekt.commands.services.CommandService;
import cz.vutbr.fit.pdb.projekt.features.persistent.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentObject;

public class ConfirmedEventAdapter<T extends PersistentObject> extends AbstractEvent<T> implements EventInterface<T> {

    public ConfirmedEventAdapter(ObjectInterface objectInterface, CommandService<T> commandService) {
        super(objectInterface, commandService);
    }

    @Override
    public T apply(T persistentObject) {
        CommandService<T> commandService = getCommandService();
        commandService.assignFromTo(getObjectInterface(), persistentObject);
        return commandService.save(persistentObject);
    }
}
