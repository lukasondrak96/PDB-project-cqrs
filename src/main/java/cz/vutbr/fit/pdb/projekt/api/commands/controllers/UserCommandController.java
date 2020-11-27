package cz.vutbr.fit.pdb.projekt.api.commands.controllers;

import cz.vutbr.fit.pdb.projekt.api.commands.dtos.user.NewUserDto;
import cz.vutbr.fit.pdb.projekt.api.commands.dtos.user.UpdateUserDto;
import cz.vutbr.fit.pdb.projekt.api.commands.services.UserCommandService;
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
    public ResponseEntity<?> activateUserAccount(@PathVariable(value = "id") String userId) {
        return null;
//        return userCommandService.activateUser(userId);
    }

    @GetMapping("{id}/deactivate")
    public ResponseEntity<?> deactivateUserAccount(@PathVariable(value = "id") String userId) {
        return null;
//        return userCommandService.deactivateUser(userId);
    }

    @PutMapping("{id}/edit")
    public ResponseEntity<?> editUserAccount(@PathVariable(value = "id") Integer userId, @Valid @RequestBody UpdateUserDto updateUserDto) {
//        return userCommandService.updateUser(userId, updateUserDto);
        return null;
    }
}
