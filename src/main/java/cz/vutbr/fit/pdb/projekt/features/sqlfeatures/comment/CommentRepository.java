package cz.vutbr.fit.pdb.projekt.features.sqlfeatures.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentTable, Integer> {
}
