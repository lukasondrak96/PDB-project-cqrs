package cz.vutbr.fit.pdb.projekt.events.events.user;

import cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices.UserService;
import cz.vutbr.fit.pdb.projekt.events.events.AbstractEvent;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.PersistentObject;

public class UserActivatedEvent<T extends PersistentObject> extends AbstractEvent<T> {

    public UserActivatedEvent(ObjectInterface objectInterface, UserService<T> commandService) {
        super(objectInterface, commandService);
    }

    @Override
    public T apply(T persistentObject) {
        UserService<T> commandService = (UserService<T>) getCommandService();
        persistentObject = commandService.assignFromTo(getObjectInterface(), persistentObject);
        return commandService.finishActivating(persistentObject);
    }

}
