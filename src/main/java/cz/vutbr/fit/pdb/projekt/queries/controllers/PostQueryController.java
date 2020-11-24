package cz.vutbr.fit.pdb.projekt.queries.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/query/posts")
public class PostQueryController {
    @GetMapping("{id}/comments")
    public ResponseEntity<?> getAllCommentsFromPost(@PathVariable(value = "id") int postId) {
        return null;
    }
}
