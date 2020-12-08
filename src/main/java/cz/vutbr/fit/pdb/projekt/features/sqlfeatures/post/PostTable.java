package cz.vutbr.fit.pdb.projekt.features.sqlfeatures.post;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.objects.PostInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentPost;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.references.UserReference;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupTable;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class PostTable implements PostInterface, PersistentPost, Comparable {

    @Id
    @SequenceGenerator(name = "PostIdGenerator", sequenceName = "POST_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PostIdGenerator")
    private int id;

    private String title;

    private String text;

    private Date createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idGroup", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private GroupTable groupReference;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserTable creator;

    public PostTable(String title, String text, Date createdAt, GroupTable groupReference, UserTable creator) {
        this.title = title;
        this.text = text;
        this.createdAt = createdAt;
        this.groupReference = groupReference;
        this.creator = creator;
    }

    @Override
    public void setCreator(UserReference userReference) {
        this.creator = (UserTable) userReference;
    }

    @Override
    public void setGroupReference(GroupTable groupReference) {
        this.groupReference = groupReference;
    }

    @Override
    public String toString() {
        return "PostTable{" +
                "id=" + id +
                ", title='" + title +
                ", text='" + text +
                ", createdAt=" + createdAt.toString() +
                '\'' +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
