package cz.vutbr.fit.pdb.projekt.commands.controllers;

import cz.vutbr.fit.pdb.projekt.commands.services.UserCommandService;
import cz.vutbr.fit.pdb.projekt.events.events.user.UserCreatedEvent;
import cz.vutbr.fit.pdb.projekt.events.subscribers.MorphiaReadCacheSubscriber;
import cz.vutbr.fit.pdb.projekt.events.subscribers.OracleSourceOfTruthSubscriber;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentUser;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import lombok.AllArgsConstructor;
import org.greenrobot.eventbus.EventBus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/commands/user")
public class UserCommandController {
    private final UserCommandService userCommandService;

    @GetMapping("test")
    public void test() {
        final EventBus eventBus = EventBus.getDefault();
        final UserDocument userDocument = new UserDocument("111", "111", 111, null, null, null, null, null );
        final UserTable userTable = new UserTable("111", "111", 111, null, null);
        new OracleSourceOfTruthSubscriber<PersistentUser>(userTable, eventBus);
        new MorphiaReadCacheSubscriber<PersistentUser>(userDocument, eventBus);
        final UserCreatedEvent createdEvent = new UserCreatedEvent(userCommandService);
        eventBus.post(createdEvent);

    }
}
