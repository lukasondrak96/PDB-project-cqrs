package cz.vutbr.fit.pdb.projekt.events.subscribers.user;

import cz.vutbr.fit.pdb.projekt.commands.services.UserCommandService;
import cz.vutbr.fit.pdb.projekt.events.events.user.UserCreatedEvent;
import cz.vutbr.fit.pdb.projekt.events.events.user.UserUpdatedEvent;
import cz.vutbr.fit.pdb.projekt.events.subscribers.AbstractSubscriber;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class SqlUserEventSubscriber<T extends UserTable> extends AbstractSubscriber<T> {

    private UserCommandService service;

    public SqlUserEventSubscriber(T user, UserCommandService service, EventBus... eventBuses) {
        super(eventBuses);
        this.persistentObject = user;
        this.service = service;
    }

    @Subscribe
    public void onUserCreatedEvent(UserCreatedEvent userCreatedEvent) {
        UserTable userTable = (UserTable) userCreatedEvent.apply(persistentObject);

        final UserDocument userDocument = new UserDocument(userTable.getIdUser(), userTable.getEmail(), userTable.getName(),
                userTable.getSurname(), userTable.getBirthDate(), userTable.getSex(), null, null, null);
        MongoUserEventSubscriber<UserDocument> mongoSubscriber = new MongoUserEventSubscriber<>(userDocument, EVENT_BUS_MONGO);
        EVENT_BUS_MONGO.post(new UserCreatedEvent(service));
        EVENT_BUS_MONGO.unregister(mongoSubscriber);
    }

    @Subscribe
    public void onUserUpdatedEvent(UserUpdatedEvent userUpdatedEvent) {
        userUpdatedEvent.apply(persistentObject);
    }

}
