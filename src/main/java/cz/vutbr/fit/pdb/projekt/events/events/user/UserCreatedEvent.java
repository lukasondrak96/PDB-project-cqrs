package cz.vutbr.fit.pdb.projekt.events.events.user;

import cz.vutbr.fit.pdb.projekt.commands.services.UserCommandService;
import cz.vutbr.fit.pdb.projekt.events.events.EventInterface;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentUser;

public class UserCreatedEvent implements EventInterface<PersistentUser> {

    private final UserCommandService service;

    public UserCreatedEvent(UserCommandService service) {
        this.service = service;
    }

    @Override
    public PersistentUser apply(PersistentUser persistentUser) {
        return service.finishUserSaving(persistentUser);
    }

    @Override
    public PersistentUser reverse(PersistentUser persistentUser) {
        return null;
    }
}
