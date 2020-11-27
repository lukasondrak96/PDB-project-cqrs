package cz.vutbr.fit.pdb.projekt.api.commands.controllers;

import cz.vutbr.fit.pdb.projekt.api.commands.dto.group.NewGroupDto;
import cz.vutbr.fit.pdb.projekt.api.commands.services.GroupCommandService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/command/groups")
public class GroupCommandController {
    private final GroupCommandService groupCommandService;


    @PostMapping("new")
    public ResponseEntity<?> createNewGroup(@Valid @RequestBody NewGroupDto newGroupDto) {
        return groupCommandService.createGroup(newGroupDto);
    }

    @PutMapping("{id}/edit")
    public ResponseEntity<?> editGroup(@PathVariable(value = "id") int groupId) {
        return null;
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<?> deleteGroup(@PathVariable(value = "id") String groupId) {
        return groupCommandService.deleteGroup(groupId);
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
