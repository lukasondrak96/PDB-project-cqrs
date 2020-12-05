package cz.vutbr.fit.pdb.projekt.api.queries.controllers;

import cz.vutbr.fit.pdb.projekt.api.queries.services.GroupQueryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/query/groups")
public class GroupQueryController {

    private final GroupQueryService groupQueryService;

    @GetMapping
    public ResponseEntity<?> getAllGroups() {
        return groupQueryService.getAllGroups();
    }

    @GetMapping("{id}/members")
    public ResponseEntity<?> getGroupMembers(@PathVariable(value = "id") int groupId) {
        return groupQueryService.getGroupMembers(groupId);
    }

    @GetMapping("{id}/admin")
    public ResponseEntity<?> getGroupAdmin(@PathVariable(value = "id") int groupId) {
        return groupQueryService.getGroupAdmin(groupId);
    }

    @GetMapping("{id}/posts")
    public ResponseEntity<?> getAllPostInGroup(@PathVariable(value = "id") int groupId) {
        return groupQueryService.getAllPostInGroup(groupId);
    }
}
