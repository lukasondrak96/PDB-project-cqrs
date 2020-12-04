package cz.vutbr.fit.pdb.projekt.api.commands.controllers;

import cz.vutbr.fit.pdb.projekt.api.commands.dtos.group.ChangeGroupStateDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.group.NewGroupDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.group.UpdateGroupDto;
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
    public ResponseEntity<?> editGroup(@PathVariable(value = "id") int groupId, @Valid @RequestBody UpdateGroupDto updateGroupDto) {
        return groupCommandService.updateGroup(groupId, updateGroupDto);
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<?> deleteGroup(@PathVariable(value = "id") int groupId) {
        return groupCommandService.deleteGroup(groupId);
    }

    @PutMapping("{id}/change-state")
    public ResponseEntity<?> changeGroupState(@PathVariable(value = "id") int groupId, @Valid @RequestBody ChangeGroupStateDto changeGroupStateDto) {
        return groupCommandService.changeGroupState(groupId, changeGroupStateDto.getState());
    }

    @GetMapping("{id}/add-user/{userId}")
    public ResponseEntity<?> addUserToGroup(@PathVariable(value = "id") int groupId,
                                            @PathVariable(value = "userId") int userId) {
        return groupCommandService.addGroupMember(groupId, userId);
    }

    @GetMapping("{id}/remove-user/{userId}")
    public ResponseEntity<?> removeUserFromGroup(@PathVariable(value = "id") int groupId,
                                                 @PathVariable(value = "userId") int userId) {
        return groupCommandService.removeGroupMember(groupId, userId);
    }

    @GetMapping("{id}/change-admin/{userId}")
    public ResponseEntity<?> changeGroupAdmin(@PathVariable(value = "id") int groupId,
                                              @PathVariable(value = "userId") int userId) {
        return groupCommandService.changeGroupAdmin(groupId, userId);
    }
}
