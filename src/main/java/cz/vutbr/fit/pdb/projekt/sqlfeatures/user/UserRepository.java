package cz.vutbr.fit.pdb.projekt.sqlfeatures.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserTable, Integer> {
}
