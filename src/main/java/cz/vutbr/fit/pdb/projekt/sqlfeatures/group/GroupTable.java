package cz.vutbr.fit.pdb.projekt.sqlfeatures.group;

import cz.vutbr.fit.pdb.projekt.sqlfeatures.user.UserTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class GroupTable {
    public GroupTable(String name, String description, String state) {
        this.name = name;
        this.description = description;
        this.state = state;
    }

    @Id
    @SequenceGenerator(name = "GroupIdGenerator", sequenceName = "GROUP_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GroupIdGenerator")
    private int idGroup;

    private String name;

    private String description;

    private String state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUser", referencedColumnName = "idUser")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserTable userTableReference;
}
