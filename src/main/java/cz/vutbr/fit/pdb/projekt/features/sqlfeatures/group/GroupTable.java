package cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.objects.GroupInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentGroup;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.references.UserReference;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupTable implements GroupInterface, PersistentGroup {
    public GroupTable(String name, String description, GroupState state, UserTable userReference) {
        this.name = name;
        this.description = description;
        this.state = state;
        this.userReference = userReference;
    }

    @Id
    @SequenceGenerator(name = "GroupIdGenerator", sequenceName = "GROUP_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GroupIdGenerator")
    private int id;

    private String name;

    private String description;

    private GroupState state;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creatorId", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserTable userReference;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_group_link",
            joinColumns = {@JoinColumn(name = "id")},
            inverseJoinColumns = {@JoinColumn(name = "idUser")}
    )
    private List<UserTable> users = new ArrayList<>();

    public void addUser(UserTable user) {
        this.users.add(user);
        user.getGroups().add(this);
    }

    public void removeUser(UserTable user) {
        this.users.removeIf(userInList -> userInList.getId() == user.getId());
        user.getGroups().removeIf(groupInList -> groupInList.getId() == this.getId());
    }

    @Override
    public void setUserReference(UserReference userReference) {
        this.userReference = (UserTable) userReference;
    }

    @Override
    public String toString() {
        return "GroupTable{" +
                "id=" + id +
                ", name='" + name +
                ", description=" + description +
                '\'' +
                '}';
    }
}
