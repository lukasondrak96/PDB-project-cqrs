package cz.vutbr.fit.pdb.projekt.sqlfeatures.post;

import cz.vutbr.fit.pdb.projekt.sqlfeatures.group.GroupTable;
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
}
