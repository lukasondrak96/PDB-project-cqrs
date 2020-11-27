package cz.vutbr.fit.pdb.projekt.api.commands.services;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentComment;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.comment.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class CommentCommandService {

    private final CommentRepository commentRepository;
    // todo private final UserDocumentRepository userDocumentRepository;

    public PersistentComment updateComment(PersistentComment persistentComment) {
        //todo
        return null;
    }

    public PersistentComment saveComment(PersistentComment persistentComment) {
        //todo
        return null;
    }

    public PersistentComment deleteComment(PersistentComment persistentComment) {
        return null;
    }
}
