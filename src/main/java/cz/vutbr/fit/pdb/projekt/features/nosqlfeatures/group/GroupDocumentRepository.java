package cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupDocumentRepository extends MongoRepository<GroupDocument, String> {
    @Query(value="{ 'creator.id' : ?0 }")
    List<GroupDocument> findByCreatorId(int id);
}
