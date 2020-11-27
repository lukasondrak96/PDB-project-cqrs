package cz.vutbr.fit.pdb.projekt.features.sqlfeatures.post;

import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupTable;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PostTable {

    public PostTable(String title, String text, Date createdAt, GroupTable groupTableReference, UserTable userTableReference) {
        this.title = title;
        this.text = text;
        this.createdAt = createdAt;
        this.groupTableReference = groupTableReference;
        this.userTableReference = userTableReference;
    }

    @Id
    @SequenceGenerator(name = "PostIdGenerator", sequenceName = "POST_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PostIdGenerator")
    private int idPost;

    private String title;

    private String text;

    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idGroup", referencedColumnName = "idGroup")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private GroupTable groupTableReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserTable userTableReference;

}
