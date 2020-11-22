package cz.vutbr.fit.pdb.projekt.sqlfeatures.comment;

import cz.vutbr.fit.pdb.projekt.sqlfeatures.post.PostTable;
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

    //TODO Id author

}
