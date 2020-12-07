package cz.vutbr.fit.pdb.projekt.events.events.user;

import cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices.UpdateCommandService;
import cz.vutbr.fit.pdb.projekt.events.events.AbstractEvent;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.PersistentObject;

public class UserUpdatedEvent<T extends PersistentObject> extends AbstractEvent<T> {

    public UserUpdatedEvent(ObjectInterface objectInterface, UpdateCommandService<T> commandService) {
        super(objectInterface, commandService);
    }

    @Override
    public T apply(T persistentObject) {
        UpdateCommandService<T> commandService = (UpdateCommandService<T>) getCommandService();
        persistentObject = commandService.assignFromTo(getObjectInterface(), persistentObject);
        return commandService.finishUpdating(persistentObject);
    }

}
