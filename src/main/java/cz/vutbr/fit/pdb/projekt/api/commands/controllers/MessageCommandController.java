package cz.vutbr.fit.pdb.projekt.api.commands.controllers;

import cz.vutbr.fit.pdb.projekt.api.commands.dtos.message.NewMessageDto;
import cz.vutbr.fit.pdb.projekt.api.commands.services.MessageCommandService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/command/messages")
public class MessageCommandController {
    private final MessageCommandService messageCommandService;
    @PostMapping("new")
    public ResponseEntity<?> createNewMessage(@Valid @RequestBody NewMessageDto newMessageDto) {
        return messageCommandService.createMessage(newMessageDto);
    }
}
