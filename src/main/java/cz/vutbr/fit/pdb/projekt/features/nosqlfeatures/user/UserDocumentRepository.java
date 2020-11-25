package cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDocumentRepository extends MongoRepository<UserDocument, Integer> {
    Optional<UserDocument> findByEmail(String email);
}
