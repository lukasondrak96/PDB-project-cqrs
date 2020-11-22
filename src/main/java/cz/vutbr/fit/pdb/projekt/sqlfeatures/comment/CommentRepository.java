package cz.vutbr.fit.pdb.projekt.sqlfeatures.comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentTable, Integer> {
}
