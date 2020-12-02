package cz.vutbr.fit.pdb.projekt.api.commands.services;

import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.GroupDocumentRepository;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocumentRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.comment.CommentRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.post.PostRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

abstract class AbstractServiceTest {


    @Autowired
    UserCommandService userCommandService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDocumentRepository userDocumentRepository;

    @Autowired
    GroupCommandService groupCommandService;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    GroupDocumentRepository groupDocumentRepository;

    @Autowired
    PostCommandService postCommandService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentCommandService commentCommandService;

    @Autowired
    CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userDocumentRepository.deleteAll();
        groupDocumentRepository.deleteAll();
        groupRepository.deleteAll();
        postRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        userDocumentRepository.deleteAll();
        groupDocumentRepository.deleteAll();
        groupRepository.deleteAll();
        postRepository.deleteAll();
        commentRepository.deleteAll();
    }

}
