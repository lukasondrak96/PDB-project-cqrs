package cz.vutbr.fit.pdb.projekt.sqlfeatures.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostTable, Integer> {
}
