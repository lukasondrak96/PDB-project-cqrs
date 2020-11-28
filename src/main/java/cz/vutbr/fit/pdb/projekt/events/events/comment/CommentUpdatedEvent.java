package cz.vutbr.fit.pdb.projekt.events.events.comment;

import cz.vutbr.fit.pdb.projekt.api.commands.services.CommentCommandService;
import cz.vutbr.fit.pdb.projekt.events.events.EventInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentComment;

public class CommentUpdatedEvent implements EventInterface<PersistentComment> {

    private final CommentCommandService service;

    public CommentUpdatedEvent(CommentCommandService service) {
        this.service = service;
    }

    @Override
    public PersistentComment apply(PersistentComment persistentComment) {
        return null;
    }

}
