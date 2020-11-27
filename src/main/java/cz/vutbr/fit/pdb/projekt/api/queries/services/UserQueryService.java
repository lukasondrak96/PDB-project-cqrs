package cz.vutbr.fit.pdb.projekt.api.queries.services;

import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.UserDocumentRepository;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserState;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserQueryService {
    private final UserDocumentRepository userDocumentRepository;

    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok().body(userDocumentRepository.findAllActiveUsers(UserState.ACTIVATED));
    }
}
