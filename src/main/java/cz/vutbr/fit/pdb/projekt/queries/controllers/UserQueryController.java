package cz.vutbr.fit.pdb.projekt.queries.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/query/users")
public class UserQueryController {
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return null;
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getInformationAboutUser(@PathVariable(value = "id") int userId) {
        return null;
    }

    @GetMapping("{id}/groups/member")
    public ResponseEntity<?> getGroupsWhereUserIsMember(@PathVariable(value = "id") int userId) {
        return null;
    }

    @GetMapping("{id}/groups/admin")
    public ResponseEntity<?> getGroupsWhereUserIsAdmin(@PathVariable(value = "id") int userId) {
        return null;
    }

    @GetMapping("{id}/conversations")
    public ResponseEntity<?> getAllUserConversations(@PathVariable(value = "id") int postId) {
        return null;
    }

    @GetMapping("{id}/conversations/{conversationId}")
    public ResponseEntity<?> getAllMessagesFromConversation(@PathVariable(value = "id") int postId,
                                                    @PathVariable(value = "conversationId") String conversationId) {
        return null;
    }
}
