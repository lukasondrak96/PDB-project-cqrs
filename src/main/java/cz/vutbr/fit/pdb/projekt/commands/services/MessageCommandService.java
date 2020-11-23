package cz.vutbr.fit.pdb.projekt.commands.services;

import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentMessage;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentPost;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentUser;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.message.MessageRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.post.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageCommandService {

    private final MessageRepository messageRepository;
    // todo private final UserDocumentRepository userDocumentRepository;

    public PersistentMessage updateMessage(PersistentMessage persistentMessage) {
        //todo
        return null;
    }

    public PersistentMessage saveMessage(PersistentMessage persistentMessage) {
        //todo
        return null;
    }

    public PersistentMessage deleteMessage(PersistentMessage persistentMessage) {
        return null;
    }
}

