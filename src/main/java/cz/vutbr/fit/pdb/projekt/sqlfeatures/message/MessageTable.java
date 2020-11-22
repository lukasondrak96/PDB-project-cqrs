package cz.vutbr.fit.pdb.projekt.sqlfeatures.message;

import cz.vutbr.fit.pdb.projekt.sqlfeatures.user.UserTable;
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

    public MessageTable(String text, Date createdAt, Date readAt, UserTable userTableReferenceSender, UserTable userTableReferenceReceiver) {
        this.text = text;
        this.createdAt = createdAt;
        this.readAt = readAt;
        this.userTableReferenceSender = userTableReferenceSender;
        this.userTableReferenceReceiver = userTableReferenceReceiver;
    }

    @Id
    @SequenceGenerator(name = "MessageIdGenerator", sequenceName = "MESSAGE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MessageIdGenerator")
    private int idMessage;

    private String text;

    private Date createdAt;

    private Date readAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUserSender", referencedColumnName = "idUser")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserTable userTableReferenceSender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUserReceiver", referencedColumnName = "idUser")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserTable userTableReferenceReceiver;
}
