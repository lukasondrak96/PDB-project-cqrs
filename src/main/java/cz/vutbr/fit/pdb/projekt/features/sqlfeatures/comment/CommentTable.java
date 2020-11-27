package cz.vutbr.fit.pdb.projekt.features.sqlfeatures.comment;

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
public class CommentTable {

    public CommentTable(String text, Date createdAt, PostTable postTableReference, UserTable userTableReference) {
        this.text = text;
        this.createdAt = createdAt;
        this.postTableReference = postTableReference;
        this.userTableReference = userTableReference;
    }

    @Id
    @SequenceGenerator(name = "CommentIdGenerator", sequenceName = "COMMENT_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CommentIdGenerator")
    private int idComment;

    private String text;

    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPost", referencedColumnName = "idPost")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PostTable postTableReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserTable userTableReference;

}
