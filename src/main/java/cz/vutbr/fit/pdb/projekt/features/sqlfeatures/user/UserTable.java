package cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user;

import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentUser;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserTable implements PersistentUser {
    public UserTable(String email, String name, String surname, Date birthDate, UserSex sex) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.sex = sex;
        this.state = UserState.ACTIVATED;
    }

    public UserTable(String email, String name, String surname, Date birthDate, UserSex sex, UserState state) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.sex = sex;
        this.state = state;
    }

    @Id
    @SequenceGenerator(name = "UserIdGenerator", sequenceName = "USER_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserIdGenerator")
    private int idUser;

    private String email;

    private String name;

    private String surname;

    private Date birthDate;

    private UserSex sex;

    private UserState state;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_group_link",
            joinColumns = {@JoinColumn(name = "idUser")},
            inverseJoinColumns = {@JoinColumn(name = "idGroup")}
    )
    private List<GroupTable> groups = new ArrayList<>();

    public void addGroup(GroupTable group) {
        this.groups.add(group);
        group.getUsers().add(this);
    }
    public void removeGroup(GroupTable group) {
        this.groups.remove(group);
        group.getUsers().remove(this);
    }
}
