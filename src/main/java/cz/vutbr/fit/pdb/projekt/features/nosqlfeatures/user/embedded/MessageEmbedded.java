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
    private Date readAt;

    @Transient
    private UserReference sender;

    @Transient
    private UserReference recipient;

}
