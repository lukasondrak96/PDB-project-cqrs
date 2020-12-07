package cz.vutbr.fit.pdb.projekt.features.sqlfeatures.message;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.objects.MessageInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentMessage;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.references.UserReference;
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
public class MessageTable implements MessageInterface, PersistentMessage {

    public MessageTable(String text, UserTable sender, UserTable recipient) {
        this.text = text;
        this.createdAt = new Date();
        this.readAt = null;
        this.sender = sender;
        this.recipient = recipient;
    }

    @Id
    @SequenceGenerator(name = "MessageIdGenerator", sequenceName = "MESSAGE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MessageIdGenerator")
    private int id;

    private String text;

    private Date createdAt;

    private Date readAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUserSender", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserTable sender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUserRecipient", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserTable recipient;

    @Override
    public String toString() {
        return "MessageTable{" +
                "id=" + id +
                ", text='" + text +
                ", createdAt=" + createdAt.toString() +
                '\'' +
                '}';
    }

    @Override
    public void setSender(UserReference sender) {
        this.sender = (UserTable) sender;
    }

    @Override
    public void setRecipient(UserReference recipient) {
        this.recipient = (UserTable) recipient;
    }
}
