package cz.vutbr.fit.pdb.projekt.commands.controllers;

import cz.vutbr.fit.pdb.projekt.commands.services.CommentCommandService;
import cz.vutbr.fit.pdb.projekt.eventsAndStuff.events.UserCreatedEvent;
import cz.vutbr.fit.pdb.projekt.eventsAndStuff.subscribers.MorphiaReadCacheSubscriber;
import cz.vutbr.fit.pdb.projekt.eventsAndStuff.subscribers.OracleSourceOfTruthSubscriber;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import org.greenrobot.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commands/comment")
public class CommentCommandController {

    @Autowired
    private CommentCommandService commentCommandService;

    @GetMapping("test")
    public void test() {
        final EventBus eventBus = EventBus.getDefault();
        final UserDocument userDocument = new UserDocument("cc", "cc", 15, null, null, null, null, null );
        final UserTable userTable = new UserTable("cc", "cc", 15, null, null);
        new OracleSourceOfTruthSubscriber(eventBus);
        new MorphiaReadCacheSubscriber(eventBus);
        final UserCreatedEvent createdEvent = new UserCreatedEvent(userTable, userDocument, commentCommandService);
        eventBus.post(createdEvent);

    }
}
