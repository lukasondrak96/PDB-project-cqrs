package cz.vutbr.fit.pdb.projekt.features.sqlfeatures.comment;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.objects.CommentInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentComment;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.references.UserReference;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.post.PostTable;
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
public class CommentTable implements CommentInterface, PersistentComment {

    public CommentTable(String text, Date createdAt, PostTable postReference, UserTable userReference) {
        this.text = text;
        this.createdAt = createdAt;
        this.postReference = postReference;
        this.userReference = userReference;
    }

    @Id
    @SequenceGenerator(name = "CommentIdGenerator", sequenceName = "COMMENT_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CommentIdGenerator")
    private int id;

    private String text;

    private Date createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idPost", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PostTable postReference;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserTable userReference;

    @Override
    public void setUserReference(UserReference userReference) {
        this.userReference = (UserTable) userReference;
    }

    @Override
    public void setPostReference(PostTable postReference) {
        this.postReference = postReference;
    }
}
