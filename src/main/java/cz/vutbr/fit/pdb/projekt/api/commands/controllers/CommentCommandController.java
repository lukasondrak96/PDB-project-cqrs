package cz.vutbr.fit.pdb.projekt.api.commands.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/command/comments")
public class CommentCommandController {
    @PostMapping("new")
    public ResponseEntity<?> createNewComment() {
        return null;
    }

    @PutMapping("{id}/edit")
    public ResponseEntity<?> editComment(@PathVariable(value = "id") int commentId) {
        return null;
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<?> deleteComment(@PathVariable(value = "id") int commentId) {
        return null;
    }
}
