package cz.vutbr.fit.pdb.projekt.events.events.user;

import cz.vutbr.fit.pdb.projekt.commands.services.PostCommandService;
import cz.vutbr.fit.pdb.projekt.commands.services.UserCommandService;
import cz.vutbr.fit.pdb.projekt.events.events.EventInterface;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentUser;

public class UserDeletedEvent implements EventInterface<PersistentUser> {

    private final UserCommandService service;

    public UserDeletedEvent(UserCommandService service) {
        this.service = service;
    }

    @Override
    public PersistentUser apply(PersistentUser persistentUser) {
        return service.deleteUser(persistentUser);
    }

    @Override
    public PersistentUser reverse(PersistentUser persistentUser) {
        return null;
    }
}
