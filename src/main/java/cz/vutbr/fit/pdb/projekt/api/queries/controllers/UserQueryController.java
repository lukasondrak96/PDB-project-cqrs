package cz.vutbr.fit.pdb.projekt.api.queries.controllers;

import cz.vutbr.fit.pdb.projekt.api.queries.services.UserQueryService;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.embedded.ConversationEmbedded;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.embedded.GroupEmbedded;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/query/users")
public class UserQueryController {

    private final UserQueryService userQueryService;

    @GetMapping()
    public ResponseEntity<List<UserDocument>> getAllUsers() {
        return userQueryService.getAllUsers();
    }

    @GetMapping("active")
    public ResponseEntity<List<UserDocument>> getAllActiveUsers() {
        return userQueryService.getAllActiveUsers();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getInformationAboutUser(@PathVariable(value = "id") int userId) {
        ResponseEntity<UserDocument> responseEntity = userQueryService.getInformationAboutUser(userId);
        if(responseEntity.getBody() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uživatel s tímto id neexistuje");
        }
        return responseEntity;
    }

    @GetMapping("{id}/groups/member")
    public ResponseEntity<?> getGroupsWhereUserIsMember(@PathVariable(value = "id") int userId) {
        ResponseEntity<List<GroupEmbedded>> responseEntity = userQueryService.getGroupsWhereUserIsMember(userId);
        if(responseEntity.getBody() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uživatel s tímto id neexistuje");
        }
        return responseEntity;
    }

    @GetMapping("{id}/groups/admin")
    public ResponseEntity<?> getGroupsWhereUserIsAdmin(@PathVariable(value = "id") int userId) {
        ResponseEntity<List<GroupEmbedded>> responseEntity = userQueryService.getGroupsWhereUserIsAdmin(userId);
        if(responseEntity.getBody() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uživatel s tímto id neexistuje");
        }
        return responseEntity;
    }

    @GetMapping("{id}/conversations")
    public ResponseEntity<?> getAllUserConversations(@PathVariable(value = "id") int userId) {
        ResponseEntity<List<ConversationEmbedded>> responseEntity = userQueryService.getAllUserConversations(userId);
        if(responseEntity.getBody() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uživatel s tímto id neexistuje");
        }
        return responseEntity;
    }

    @GetMapping("{id}/conversations/{otherUserId}")
    public ResponseEntity<?> getAllMessagesFromConversation(@PathVariable(value = "id") int userId,
                                                    @PathVariable(value = "otherUserId") int anotherUserId) {
        ResponseEntity<List<ConversationEmbedded>> responseEntity = userQueryService.getAllMessagesFromConversation(userId, anotherUserId);
        if(responseEntity.getBody() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uživatel s tímto id neexistuje");
        }
        return responseEntity;
    }
}
