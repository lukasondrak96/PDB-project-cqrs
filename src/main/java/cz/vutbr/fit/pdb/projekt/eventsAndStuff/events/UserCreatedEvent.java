package cz.vutbr.fit.pdb.projekt.eventsAndStuff.events;

import cz.vutbr.fit.pdb.projekt.commands.services.CommentCommandService;
import cz.vutbr.fit.pdb.projekt.eventsAndStuff.EventInterface;
import cz.vutbr.fit.pdb.projekt.features.PersistentUser;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;

public class UserCreatedEvent implements EventInterface {

    private final UserTable userSql;
    private final UserDocument userNoSql;

    private final CommentCommandService service;

    public UserCreatedEvent(UserTable userSql, UserDocument userNoSql, CommentCommandService service) {
        this.service = service;
        this.userSql = userSql;
        this.userNoSql = userNoSql;
    }

    @Override
    public PersistentUser apply(PersistentUser persistentUser) {
        if(persistentUser instanceof UserTable)
            return service.doMagic(userSql);
        else
            return service.doMagic(userNoSql);
    }

    @Override
    public PersistentUser reverse(PersistentUser persistentBooking) {
        //todo
        return null;
    }
}
