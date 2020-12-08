package cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<GroupTable, Integer> {

    int countByName(String name);

    Optional<GroupTable> findByName(String name);

    List<GroupTable> findAllByCreator_Id(int id);
}
