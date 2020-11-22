package cz.vutbr.fit.pdb.projekt.nosqlfeatures.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationInherited {
    private String name;
    private String surname;
    private List<MessageInherited> messages;
}
