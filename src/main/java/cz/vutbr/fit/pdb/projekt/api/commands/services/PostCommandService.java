package cz.vutbr.fit.pdb.projekt.api.commands.services;

import cz.vutbr.fit.pdb.projekt.api.commands.dtos.post.NewPostDto;
import cz.vutbr.fit.pdb.projekt.api.commands.services.helpingservices.CommandService;
import cz.vutbr.fit.pdb.projekt.events.events.OracleCreatedEvent;
import cz.vutbr.fit.pdb.projekt.events.subscribers.post.OraclePostEventSubscriber;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.objects.PostInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentPost;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupTable;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.post.PostRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.post.PostTable;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import lombok.AllArgsConstructor;
import org.greenrobot.eventbus.EventBus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;


@Service
@AllArgsConstructor
public class PostCommandService implements CommandService<PersistentPost> {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    private static final EventBus EVENT_BUS = EventBus.getDefault();

    public ResponseEntity<?> createPost(NewPostDto newPostDto) {
        Optional<UserTable> userOptional = userRepository.findById(newPostDto.getCreatorId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uživatel s tímto id neexistuje");
        }
        UserTable creator = userOptional.get();

        Optional<GroupTable> groupOptional = groupRepository.findById(newPostDto.getGroupId());
        if (groupOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Skupina s tímto id neexistuje");
        }
        GroupTable group = groupOptional.get();

        final PostTable postTable = new PostTable(newPostDto.getTitle(), newPostDto.getText(), new Date(), group, creator);

        OraclePostEventSubscriber oracleSubscriber = new OraclePostEventSubscriber(EVENT_BUS);
//        MongoPostEventSubscriber mongoSubscriber = new MongoPostEventSubscriber(EVENT_BUS);

        EVENT_BUS.post(new OracleCreatedEvent<>(postTable, this));
        EVENT_BUS.unregister(oracleSubscriber);
//        EVENT_BUS.unregister(mongoSubscriber);

        return ResponseEntity.ok().body("Příspěvek byl vytvořen");
    }


    @Override
    public PersistentPost assignFromTo(ObjectInterface objectInterface, PersistentPost post) {
        PostInterface persistentPostInterface = (PostInterface) post;
        PostInterface postInterface = (PostInterface) objectInterface;
        if(post instanceof PostTable) {
            persistentPostInterface.setId(postInterface.getId());
            persistentPostInterface.setTitle(postInterface.getTitle());
            persistentPostInterface.setText(postInterface.getText());
            persistentPostInterface.setCreatedAt(postInterface.getCreatedAt());
            persistentPostInterface.setGroupTableReference(postInterface.getGroupTableReference());
            persistentPostInterface.setUserTableReference(postInterface.getUserTableReference());
        }
        return (PersistentPost) persistentPostInterface;
    }

    @Override
    public PersistentPost finishSaving(PersistentPost post) {
//        if (post instanceof PostTable)
            return postRepository.save((PostTable) post);
//        else
//            return groupDocumentRepository.save((GroupDocument) group);
    }

    @Override
    public PersistentPost finishUpdating(PersistentPost persistentObject) {
        return null;
    }
}
