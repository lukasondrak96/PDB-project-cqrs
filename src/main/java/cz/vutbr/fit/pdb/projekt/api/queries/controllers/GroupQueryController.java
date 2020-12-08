package cz.vutbr.fit.pdb.projekt.api.queries.controllers;

import cz.vutbr.fit.pdb.projekt.api.queries.services.GroupQueryService;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.CreatorEmbedded;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.MemberEmbedded;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.PostEmbedded;
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
@RequestMapping("/query/groups")
public class GroupQueryController {

    private final GroupQueryService groupQueryService;

    @GetMapping
    public ResponseEntity<?> getAllGroups() {
        return groupQueryService.getAllGroups();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getInformationAboutGroup(@PathVariable(value = "id") int groupId) {
        ResponseEntity<GroupDocument> responseEntity = groupQueryService.getInformationAboutGroup(groupId);
        if(responseEntity.getBody() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Skupina s tímto id neexistuje");
        }
        return responseEntity;
    }

    @GetMapping("{id}/members")
    public ResponseEntity<?> getGroupMembers(@PathVariable(value = "id") int groupId) {
        ResponseEntity<List<MemberEmbedded>> responseEntity = groupQueryService.getGroupMembers(groupId);
        if(responseEntity.getBody() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Skupina s tímto id neexistuje");
        }
        return responseEntity;
    }

    @GetMapping("{id}/admin")
    public ResponseEntity<?> getGroupAdmin(@PathVariable(value = "id") int groupId) {
        ResponseEntity<CreatorEmbedded> responseEntity = groupQueryService.getGroupAdmin(groupId);
        if(responseEntity.getBody() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Skupina s tímto id neexistuje");
        }
        return responseEntity;
    }

    @GetMapping("{id}/posts")
    public ResponseEntity<?> getAllPostInGroup(@PathVariable(value = "id") int groupId) {
        ResponseEntity<List<PostEmbedded>> responseEntity = groupQueryService.getAllPostInGroup(groupId);
        if(responseEntity.getBody() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Skupina s tímto id neexistuje");
        }
        return responseEntity;
    }
}
