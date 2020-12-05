package cz.vutbr.fit.pdb.projekt.api.queries.services;

import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocumentRepository;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.CommentEmbedded;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
@AllArgsConstructor
public class PostQueryService {

    private final GroupDocumentRepository groupDocumentRepository;
    private final MongoTemplate mongoTemplate;


    public ResponseEntity<List<CommentEmbedded>> getAllCommentsFromPost(int postId) {
        List<GroupDocument> groupDocumentsWithPost = mongoTemplate.find(
                new Query(where("posts.id").is(postId)),
                GroupDocument.class
        );
        if(groupDocumentsWithPost.size() == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        AtomicReference<List<CommentEmbedded>> comments = new AtomicReference<>();

        groupDocumentsWithPost.get(0).getPosts().forEach(post -> {
            if(post.getId() == postId) {
                comments.set(post.getComments());
            }
        });
        if(comments.get() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        else
            return ResponseEntity.ok().body(comments.get());
    }
}
