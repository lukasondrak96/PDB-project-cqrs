package cz.vutbr.fit.pdb.projekt.sqlfeatures.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository  extends JpaRepository<MessageTable, Integer> {
}
