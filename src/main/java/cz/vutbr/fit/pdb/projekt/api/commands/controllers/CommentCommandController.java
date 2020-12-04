package cz.vutbr.fit.pdb.projekt.api.commands.controllers;

import cz.vutbr.fit.pdb.projekt.api.commands.dtos.comment.NewCommentDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.comment.UpdateCommentDto;
import cz.vutbr.fit.pdb.projekt.api.commands.services.CommentCommandService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/command/comments")
public class CommentCommandController {
    private final CommentCommandService commentCommandService;

    @PostMapping("new")
    public ResponseEntity<?> createNewComment(@Valid @RequestBody NewCommentDto newCommentDto) {
        return commentCommandService.createComment(newCommentDto);
    }

    @PutMapping("{id}/edit")
    public ResponseEntity<?> editComment(@PathVariable(value = "id") int commentId, @Valid @RequestBody UpdateCommentDto updateCommentDto) {
        return commentCommandService.updateComment(commentId, updateCommentDto);
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<?> deleteComment(@PathVariable(value = "id") int commentId) {
        return commentCommandService.deleteComment(commentId);
    }
}
