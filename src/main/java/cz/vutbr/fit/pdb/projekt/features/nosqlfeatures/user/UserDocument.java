package cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.objects.UserInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentUser;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.embedded.ConversationEmbedded;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.embedded.GroupEmbedded;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserSex;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserState;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Document
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDocument implements UserInterface, PersistentUser {

    @Id
    private int id;

    public UserDocument(int id, String email, String name, String surname, Date birthDate, UserSex sex, List<GroupEmbedded> groupsMember,
                        List<GroupEmbedded> groupsAdmin, List<ConversationEmbedded> conversationsWithUser) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.sex = sex;
        this.state = UserState.ACTIVATED;
        this.groupsMember = groupsMember;
        this.groupsAdmin = groupsAdmin;
        this.conversationsWithUser = conversationsWithUser;
    }

    public UserDocument(int id, String email, String name, String surname, Date birthDate, UserSex sex, UserState state,
                        List<GroupEmbedded> groupsMember, List<GroupEmbedded> groupsAdmin, List<ConversationEmbedded> conversationsWithUser) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.sex = sex;
        this.state = state;
        this.groupsMember = groupsMember;
        this.groupsAdmin = groupsAdmin;
        this.conversationsWithUser = conversationsWithUser;
    }

    private List<ConversationEmbedded> conversationsWithUser;
    private String email;
    private String name;
    private String surname;
    private Date birthDate;
    private UserSex sex;
    private UserState state;
    private List<GroupEmbedded> groupsMember;
    private List<GroupEmbedded> groupsAdmin;

}
