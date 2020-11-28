package cz.vutbr.fit.pdb.projekt.events.events.post;

import cz.vutbr.fit.pdb.projekt.api.commands.services.PostCommandService;
import cz.vutbr.fit.pdb.projekt.events.events.EventInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentPost;

public class PostUpdatedEvent implements EventInterface<PersistentPost> {

    private final PostCommandService service;

    public PostUpdatedEvent(PostCommandService service) {
        this.service = service;
    }

    @Override
    public PersistentPost apply(PersistentPost persistentPost) {
        return null;
    }

}
