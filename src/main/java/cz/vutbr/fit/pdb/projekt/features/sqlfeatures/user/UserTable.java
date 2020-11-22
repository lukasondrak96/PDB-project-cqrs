package cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user;

import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    private String name;

    private String surname;

    private int age;

    private String sex;

    private String state;

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
