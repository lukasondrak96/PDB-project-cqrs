package cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserTable, Integer> {
    int countByEmail(String email);

    @Modifying
    @Query("update UserTable u set u.email = :email, u.name = :name, u.surname = :surname, u.birthDate = :birthDate, u.sex = :sex, u.state = :state where u.idUser = :id")
    UserTable updateUser(int id, String email, String name, String surname, Date birthDate, UserSex sex, UserState state);

    Optional<UserTable> findByEmail(String email);
}
