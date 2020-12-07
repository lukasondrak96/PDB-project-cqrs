package cz.vutbr.fit.pdb.projekt.features.sqlfeatures.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository  extends JpaRepository<MessageTable, Integer> {

    List<MessageTable> findByText(String text);
}
