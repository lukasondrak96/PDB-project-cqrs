package cz.vutbr.fit.pdb.projekt.events.events;

import cz.vutbr.fit.pdb.projekt.api.commands.services.CommandService;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.PersistentObject;

public class OracleCreatedEvent<T extends PersistentObject> extends AbstractEvent<T> {

    public OracleCreatedEvent(ObjectInterface objectInterface, CommandService<T> commandService) {
        super(objectInterface, commandService);
    }

    @Override
    public T apply(T persistentObject) {
        CommandService<T> commandService = getCommandService();
        persistentObject = commandService.assignFromTo(getObjectInterface(), persistentObject);
        return commandService.save(persistentObject);
    }

}
