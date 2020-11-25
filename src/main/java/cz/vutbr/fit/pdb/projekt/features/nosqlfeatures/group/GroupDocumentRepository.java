package cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupDocumentRepository extends MongoRepository<GroupDocument, String> {
}
