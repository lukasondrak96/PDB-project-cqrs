package cz.vutbr.fit.pdb.projekt.sqlfeatures.group;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    //TODO Id creator
}
