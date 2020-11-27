package cz.vutbr.fit.pdb.projekt.api.commands.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/command/messages")
public class MessageCommandController {
    @PostMapping("new")
    public ResponseEntity<?> createNewMessage() {
        return null;
    }
}
