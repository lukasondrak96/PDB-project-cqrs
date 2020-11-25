package cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user;

import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserState;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserDocumentRepository extends MongoRepository<UserDocument, Integer> {
    Optional<UserDocument> findByEmail(String email);

    @Query(value="{ 'state' : ?0 }", fields="{ 'email' : 1, 'name' : 1, 'surname' : 1}")
    List<UserDocument> findAllActiveUsers(UserState state);
}
