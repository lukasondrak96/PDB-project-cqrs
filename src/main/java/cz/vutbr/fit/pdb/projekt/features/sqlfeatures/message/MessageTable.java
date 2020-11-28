package cz.vutbr.fit.pdb.projekt.features.sqlfeatures.message;

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
public class MessageTable {

    public MessageTable(String text, Date createdAt, Date readAt, UserTable userReferenceSender, UserTable userReferenceReceiver) {
        this.text = text;
        this.createdAt = createdAt;
        this.readAt = readAt;
        this.userReferenceSender = userReferenceSender;
        this.userReferenceReceiver = userReferenceReceiver;
    }

    @Id
    @SequenceGenerator(name = "MessageIdGenerator", sequenceName = "MESSAGE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MessageIdGenerator")
    private int idMessage;

    private String text;

    private Date createdAt;

    private Date readAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUserSender", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserTable userReferenceSender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUserReceiver", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserTable userReferenceReceiver;
}
