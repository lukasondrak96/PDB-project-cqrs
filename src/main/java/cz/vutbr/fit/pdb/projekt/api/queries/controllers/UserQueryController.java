package cz.vutbr.fit.pdb.projekt.api.queries.controllers;

import cz.vutbr.fit.pdb.projekt.api.queries.services.UserQueryService;
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
    private final UserQueryService userQueryService;
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return userQueryService.getAllUsers();
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
