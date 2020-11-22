package cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.inherited;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageInherited {
    private boolean sent;
    private String text;
    private Date createdAt;
    private Date readAt;
}