package cz.vutbr.fit.pdb.projekt.sqlfeatures.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserTable, Integer> {
}
