package cz.vutbr.fit.pdb.projekt.queries.controllers;

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
    @GetMapping
    public ResponseEntity<?> getAllGroups() {
        return null;
    }

    @GetMapping("{id}/members")
    public ResponseEntity<?> getGroupMembers(@PathVariable(value = "id") int groupId) {
        return null;
    }

    @GetMapping("{id}/admin")
    public ResponseEntity<?> getGroupAdmin(@PathVariable(value = "id") int groupId) {
        return null;
    }

    @GetMapping("{id}/posts")
    public ResponseEntity<?> getAllPostInGroup(@PathVariable(value = "id") int groupId) {
        return null;
    }
}
