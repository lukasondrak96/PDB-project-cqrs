package cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.embedded;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.objects.MessageInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentMessage;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.references.UserReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageEmbedded implements MessageInterface, PersistentMessage {

    private int id;
    private String text;
    private Date createdAt;
    private boolean sent;

    public MessageEmbedded(int id, String text, Date createdAt, UserReference sender, UserReference recipient) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.sender = sender;
        this.recipient = recipient;
    }

    @Transient
    private UserReference sender;

    @Transient
    private UserReference recipient;


}
