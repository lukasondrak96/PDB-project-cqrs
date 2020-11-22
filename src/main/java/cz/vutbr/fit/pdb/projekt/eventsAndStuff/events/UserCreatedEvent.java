package cz.vutbr.fit.pdb.projekt.eventsAndStuff.events;

import cz.vutbr.fit.pdb.projekt.commands.services.CommentCommandService;
import cz.vutbr.fit.pdb.projekt.commands.services.UserCommandService;
import cz.vutbr.fit.pdb.projekt.eventsAndStuff.EventInterface;
import cz.vutbr.fit.pdb.projekt.features.PersistentUser;

public class UserCreatedEvent implements EventInterface {

    private final UserCommandService service;

    public UserCreatedEvent(UserCommandService service) {
        this.service = service;
    }

    @Override
    public PersistentUser apply(PersistentUser persistentUser) {
            return service.saveUser(persistentUser);
   }

    @Override
    public PersistentUser reverse(PersistentUser persistentBooking) {
        //todo
        return null;
    }
}
