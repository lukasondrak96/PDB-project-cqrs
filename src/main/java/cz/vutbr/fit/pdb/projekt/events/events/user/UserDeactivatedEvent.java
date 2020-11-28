package cz.vutbr.fit.pdb.projekt.events.events.user;

import cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices.UserWithStateChangingService;
import cz.vutbr.fit.pdb.projekt.events.events.AbstractEvent;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.PersistentObject;

public class UserDeactivatedEvent<T extends PersistentObject> extends AbstractEvent<T> {

    public UserDeactivatedEvent(ObjectInterface objectInterface, UserWithStateChangingService<T> commandService) {
        super(objectInterface, commandService);
    }

    @Override
    public T apply(T persistentObject) {
        UserWithStateChangingService<T> commandService = (UserWithStateChangingService<T>) getCommandService();
        persistentObject = commandService.assignFromTo(getObjectInterface(), persistentObject);
        return commandService.finishDeactivating(persistentObject);
    }

}
