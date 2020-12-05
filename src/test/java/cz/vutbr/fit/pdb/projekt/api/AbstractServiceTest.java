package cz.vutbr.fit.pdb.projekt.api;

import cz.vutbr.fit.pdb.projekt.api.commands.services.*;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocumentRepository;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocumentRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.comment.CommentRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.message.MessageRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.post.PostRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractServiceTest {


    @Autowired
    public UserCommandService userCommandService;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public UserDocumentRepository userDocumentRepository;

    @Autowired
    public GroupCommandService groupCommandService;

    @Autowired
    public GroupRepository groupRepository;

    @Autowired
    public GroupDocumentRepository groupDocumentRepository;

    @Autowired
    public PostCommandService postCommandService;

    @Autowired
    public PostRepository postRepository;

    @Autowired
    public CommentCommandService commentCommandService;

    @Autowired
    public CommentRepository commentRepository;

    @Autowired
    public MessageCommandService messageCommandService;

    @Autowired
    public MessageRepository messageRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        userDocumentRepository.deleteAll();
        groupDocumentRepository.deleteAll();
        groupRepository.deleteAll();
        postRepository.deleteAll();
        commentRepository.deleteAll();
        messageRepository.deleteAll();
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        userDocumentRepository.deleteAll();
        groupDocumentRepository.deleteAll();
        groupRepository.deleteAll();
        postRepository.deleteAll();
        commentRepository.deleteAll();
        messageRepository.deleteAll();
    }

}
