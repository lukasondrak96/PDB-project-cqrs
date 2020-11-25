package cz.vutbr.fit.pdb.projekt.commands.controllers;

import cz.vutbr.fit.pdb.projekt.commands.dto.user.NewUserDto;
import cz.vutbr.fit.pdb.projekt.commands.services.UserCommandService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/command/users")
public class UserCommandController {
    private final UserCommandService userCommandService;

    @PostMapping("new")
    public ResponseEntity<?> createNewUserAccount(@Valid @RequestBody NewUserDto newUserDto) {
        return userCommandService.createUser(newUserDto);
    }

    @GetMapping("{id}/activate")
    public ResponseEntity<?> activateUserAccount(@PathVariable(value = "id") int userId) {
        return null;
    }

    @GetMapping("{id}/deactivate")
    public ResponseEntity<?> deactivateUserAccount(@PathVariable(value = "id") int userId) {
        return null;
    }

    @PutMapping("{id}/edit")
    public ResponseEntity<?> editUserAccount(@PathVariable(value = "id") int userId) {
        return null;
    }
}
