package cz.vutbr.fit.pdb.projekt.commands.controllers;

import cz.vutbr.fit.pdb.projekt.commands.services.UserCommandService;
import cz.vutbr.fit.pdb.projekt.events.events.user.UserCreatedEvent;
import cz.vutbr.fit.pdb.projekt.events.subscribers.UserEventSubscriber;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
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

        final UserDocument userDocument = new UserDocument("testtesttest", "111", 111, null, null, null, null, null );
        new UserEventSubscriber<>(userDocument, eventBus);

        final UserTable userTable = new UserTable("testtesttest", "111", 111, null, null);
        new UserEventSubscriber<>(userTable, eventBus);

        final UserCreatedEvent createdEvent = new UserCreatedEvent(userCommandService);
        eventBus.post(createdEvent);

    }
}
