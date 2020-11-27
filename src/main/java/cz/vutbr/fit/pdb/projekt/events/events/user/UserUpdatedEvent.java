package cz.vutbr.fit.pdb.projekt.events.events.user;

import cz.vutbr.fit.pdb.projekt.api.commands.services.UserCommandService;
import cz.vutbr.fit.pdb.projekt.events.events.EventInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentUser;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserUpdatedEvent implements EventInterface<PersistentUser> {

    private final UserCommandService service;

    @Override
    public PersistentUser apply(PersistentUser persistentUser) {
//        return service.finishUpdating(persistentUser);
        return null;
    }

}
