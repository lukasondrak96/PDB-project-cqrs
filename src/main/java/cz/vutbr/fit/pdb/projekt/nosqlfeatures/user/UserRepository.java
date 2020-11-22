package cz.vutbr.fit.pdb.projekt.nosqlfeatures.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserDocument, Integer> {
}
