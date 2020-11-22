package cz.vutbr.fit.pdb.projekt.nosqlfeatures.user;

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
