package cz.vutbr.fit.pdb.projekt.events.events.post;

import cz.vutbr.fit.pdb.projekt.api.commands.services.PostCommandService;
import cz.vutbr.fit.pdb.projekt.events.events.EventInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentPost;

public class PostCreatedEvent implements EventInterface<PersistentPost> {

    private final PostCommandService service;

    public PostCreatedEvent(PostCommandService service) {
        this.service = service;
    }

    @Override
    public PersistentPost apply(PersistentPost persistentPost) {
        return service.savePost(persistentPost);
    }

}
