package cz.vutbr.fit.pdb.projekt.commands.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/command/groups")
public class GroupCommandController {
    @PostMapping("new")
    public ResponseEntity<?> createNewGroup() {
        return null;
    }

    @PutMapping("{id}/edit")
    public ResponseEntity<?> editGroup(@PathVariable(value = "id") int groupId) {
        return null;
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<?> deleteGroup(@PathVariable(value = "id") int groupId) {
        return null;
    }

    @PutMapping("{id}/change-state")
    public ResponseEntity<?> changeGroupState(@PathVariable(value = "id") int groupId) {
        return null;
    }

    @GetMapping("{id}/add-user/{userId}")
    public ResponseEntity<?> addUserToGroup(@PathVariable(value = "id") int groupId,
                                            @PathVariable(value = "userId") String userId) {
        return null;
    }

    @GetMapping("{id}/remove-user/{userId}")
    public ResponseEntity<?> removeUserToGroup(@PathVariable(value = "id") int groupId,
                                               @PathVariable(value = "userId") String userId) {
        return null;
    }

    @GetMapping("{id}/change-admin/{userId}")
    public ResponseEntity<?> changeGroupAdmin(@PathVariable(value = "id") int groupId,
                                              @PathVariable(value = "userId") String userId) {
        return null;
    }
}
