package cz.vutbr.fit.pdb.projekt.api.queries.services;

import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocument;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocumentRepository;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.embedded.GroupEmbedded;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserState;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserQueryService {
    private final UserDocumentRepository userDocumentRepository;

    public ResponseEntity<List<UserDocument>> getAllUsers() {
        return ResponseEntity.ok().body(userDocumentRepository.findAll());
    }

    public ResponseEntity<List<UserDocument>> getAllActiveUsers() {
        return ResponseEntity.ok().body(userDocumentRepository.findAllActiveUsers(UserState.ACTIVATED));
    }

    public ResponseEntity<UserDocument> getInformationAboutUser(int userId) {
        Optional<UserDocument> userDocumentOptional = userDocumentRepository.findById(userId);
        if(userDocumentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok().body(userDocumentOptional.get());
    }

    public ResponseEntity<List<GroupEmbedded>> getGroupsWhereUserIsMember(int userId) {
        Optional<UserDocument> userDocumentOptional = userDocumentRepository.findById(userId);
        if(userDocumentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok().body(userDocumentOptional.get().getGroupsMember());
    }

    public ResponseEntity<List<GroupEmbedded>> getGroupsWhereUserIsAdmin(int userId) {
        Optional<UserDocument> userDocumentOptional = userDocumentRepository.findById(userId);
        if(userDocumentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok().body(userDocumentOptional.get().getGroupsAdmin());
    }

}
