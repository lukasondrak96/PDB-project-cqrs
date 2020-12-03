package cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationEmbedded {

    private int id;
    private String name;
    private String surname;
    private List<MessageEmbedded> messages;

}
