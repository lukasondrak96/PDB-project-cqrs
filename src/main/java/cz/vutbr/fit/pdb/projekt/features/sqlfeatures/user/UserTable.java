package cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.objects.UserInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentUser;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.references.UserReference;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTable implements UserInterface, PersistentUser, UserReference {
    public UserTable(String email, String name, String surname, Date birthDate, UserSex sex) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.sex = sex;
        this.state = UserState.ACTIVATED;
    }

    @Id
    @SequenceGenerator(name = "UserIdGenerator", sequenceName = "USER_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserIdGenerator")
    private int id;

    private String email;

    private String name;

    private String surname;

    private Date birthDate;

    private UserSex sex;

    private UserState state;

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<GroupTable> groups = new ArrayList<>();

    public void addGroup(GroupTable group) {
        this.groups.add(group);
        group.getUsers().add(this);
    }
    public void removeGroup(GroupTable group) {
        this.groups.remove(group);
        group.getUsers().remove(this);
    }

    @Override
    public String toString() {
        return "UserTable{" +
                "id=" + id +
                ", email='" + email +
                ", name=" + name +
                ", surname=" + surname +
                ", birthDate=" + birthDate.toString() +
                ", sex=" + sex.toString() +
                ", state=" + state.toString() +
                '\'' +
                '}';
    }
}
