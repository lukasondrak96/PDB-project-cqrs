package cz.vutbr.fit.pdb.projekt.api.queries.controllers;

import cz.vutbr.fit.pdb.projekt.api.queries.services.PostQueryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/query/posts")
public class PostQueryController {

    private final PostQueryService postQueryService;


    @GetMapping("{id}/comments")
    public ResponseEntity<?> getAllCommentsFromPost(@PathVariable(value = "id") int postId) {
        return postQueryService.getAllCommentsFromPost(postId);
    }
}
