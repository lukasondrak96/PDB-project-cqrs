package cz.vutbr.fit.pdb.projekt.events.events;

import cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices.CreateCommandService;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.PersistentObject;

public class OracleCreatedEvent<T extends PersistentObject> extends AbstractEvent<T> {

    public OracleCreatedEvent(ObjectInterface objectInterface, CreateCommandService<T> commandService) {
        super(objectInterface, commandService);
    }

    @Override
    public T apply(T persistentObject) {
        CreateCommandService<T> commandService = getCommandService();
        persistentObject = commandService.assignFromTo(getObjectInterface(), persistentObject);
        return commandService.finishSaving(persistentObject);
    }

}
