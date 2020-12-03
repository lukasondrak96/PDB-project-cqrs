package cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageEmbedded {

    private int id;
    private boolean sent;
    private String text;
    private Date createdAt;
    private Date readAt;

}
