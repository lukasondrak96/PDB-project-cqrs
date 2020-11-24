package cz.vutbr.fit.pdb.projekt.commands.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/command/posts")
public class PostCommandController {
    @PostMapping("new")
    public ResponseEntity<?> createNewPost() {
        return null;
    }

    @PutMapping("{id}/edit")
    public ResponseEntity<?> editPost(@PathVariable(value = "id") int postId) {
        return null;
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<?> deletePost(@PathVariable(value = "id") int postId) {
        return null;
    }
}
