package cz.vutbr.fit.pdb.projekt.events.events.user;

import cz.vutbr.fit.pdb.projekt.commands.services.UserCommandService;
import cz.vutbr.fit.pdb.projekt.events.events.EventInterface;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentUser;

public class UserUpdatedEvent implements EventInterface<PersistentUser> {

    private final UserCommandService service;

    public UserUpdatedEvent(UserCommandService service) {
        this.service = service;
    }

    @Override
    public PersistentUser apply(PersistentUser persistentUser) {
        return service.finishUserUpdating(persistentUser);
    }

}
