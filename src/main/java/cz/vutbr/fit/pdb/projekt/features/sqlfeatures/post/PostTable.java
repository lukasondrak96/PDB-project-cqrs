package cz.vutbr.fit.pdb.projekt.features.sqlfeatures.post;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.objects.PostInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentPost;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.references.UserReference;
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
public class PostTable implements PostInterface, PersistentPost {

    public PostTable(String title, String text, Date createdAt, GroupTable groupReference, UserTable userReference) {
        this.title = title;
        this.text = text;
        this.createdAt = createdAt;
        this.groupReference = groupReference;
        this.userReference = userReference;
    }

    @Id
    @SequenceGenerator(name = "PostIdGenerator", sequenceName = "POST_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PostIdGenerator")
    private int id;

    private String title;

    private String text;

    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idGroup", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private GroupTable groupReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserTable userReference;

    @Override
    public void setUserReference(UserReference userReference) {
        this.userReference = (UserTable) userReference;
    }

    @Override
    public void setGroupReference(GroupTable groupReference) {
        this.groupReference = groupReference;
    }
}
