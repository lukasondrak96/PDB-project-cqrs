package cz.vutbr.fit.pdb.projekt.api.queries.controllers;

import cz.vutbr.fit.pdb.projekt.api.queries.services.PostQueryService;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.CommentEmbedded;
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
@RequestMapping("/query/posts")
public class PostQueryController {

    private final PostQueryService postQueryService;

    @GetMapping("{id}/comments")
    public ResponseEntity<?> getAllCommentsFromPost(@PathVariable(value = "id") int postId) {
        ResponseEntity<List<CommentEmbedded>> responseEntity = postQueryService.getAllCommentsFromPost(postId);
        if(responseEntity.getBody() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Příspěvek s tímto id neexistuje");
        }
        return responseEntity;
    }
}
