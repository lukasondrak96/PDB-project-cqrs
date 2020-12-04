package cz.vutbr.fit.pdb.projekt.api.commands.controllers;

import cz.vutbr.fit.pdb.projekt.api.commands.dtos.post.NewPostDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.post.UpdatePostDto;
import cz.vutbr.fit.pdb.projekt.api.commands.services.PostCommandService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/command/posts")
public class PostCommandController {
    private final PostCommandService postCommandService;

    @PostMapping("new")
    public ResponseEntity<?> createNewPost(@Valid @RequestBody NewPostDto newPostDto) {
        return postCommandService.createPost(newPostDto);
    }

    @PutMapping("{id}/edit")
    public ResponseEntity<?> editPost(@PathVariable(value = "id") int postId, @Valid @RequestBody UpdatePostDto updatePostDto) {
        return postCommandService.updatePost(postId, updatePostDto);
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<?> deletePost(@PathVariable(value = "id") int postId) {
        return postCommandService.deletePost(postId);
    }
}
