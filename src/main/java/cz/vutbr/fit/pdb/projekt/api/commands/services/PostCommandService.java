package cz.vutbr.fit.pdb.projekt.api.commands.services;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentPost;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.post.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class PostCommandService {

    private final PostRepository postRepository;
    // todo private final UserDocumentRepository userDocumentRepository;

    public PersistentPost updatePost(PersistentPost persistentPost) {
        //todo
        return null;
    }

    public PersistentPost savePost(PersistentPost persistentPost) {
        //todo
        return null;
    }

    public PersistentPost deletePost(PersistentPost persistentPost) {
        return null;
    }
}
