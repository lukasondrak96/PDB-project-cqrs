package cz.vutbr.fit.pdb.projekt.commands.controllers;

import cz.vutbr.fit.pdb.projekt.commands.services.UserCommandService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/command/users")
public class UserCommandController {
    private final UserCommandService userCommandService;

    @PostMapping("new")
    public ResponseEntity<?> createNewUserAccount() {
//        final EventBus eventBus = EventBus.getDefault();
//
//        final UserDocument userDocument = new UserDocument("testtesttest", "111", 111, null, null, null, null, null );
//        new UserEventSubscriber<>(userDocument, eventBus);
//
//        final UserTable userTable = new UserTable("testtesttest", "111", 111, null, null);
//        new UserEventSubscriber<>(userTable, eventBus);
//
//        final UserCreatedEvent createdEvent = new UserCreatedEvent(userCommandService);
//        eventBus.post(createdEvent);
        return null;
    }

    @GetMapping("{id}/activate")
    public ResponseEntity<?> activateUserAccount(@PathVariable(value = "id") int userId) {
        return null;
    }

    @GetMapping("{id}/deactivate")
    public ResponseEntity<?> deactivateUserAccount(@PathVariable(value = "id") int userId) {
        return null;
    }

    @PutMapping("{id}/edit")
    public ResponseEntity<?> editUserAccount(@PathVariable(value = "id") int userId) {
        return null;
    }
}
