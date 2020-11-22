package cz.vutbr.fit.pdb.projekt.sqlfeatures.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserTable {
    public UserTable(String name, String surname, int age, String sex, String state) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.sex = sex;
        this.state = state;
    }

    @Id
    @SequenceGenerator(name = "UserIdGenerator", sequenceName = "USER_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserIdGenerator")
    private int idUser;

    String name;

    String surname;

    int age;

    String sex;

    String state;
}
