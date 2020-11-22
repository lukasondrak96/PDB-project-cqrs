package cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDocumentRepository extends MongoRepository<UserDocument, Integer> {
}
