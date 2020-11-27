package cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.inherited.ConversationInherited;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.inherited.GroupInherited;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentUser;
import cz.vutbr.fit.pdb.projekt.features.persistent.UserInterface;
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

    public UserDocument(int idUser, String email, String name, String surname, Date birthDate, UserSex sex, List<GroupInherited> groupsMember,
                        List<GroupInherited> groupsAdmin, List<ConversationInherited> conversationsWithUser) {
        this.idUser = idUser;
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

    public UserDocument(int idUser, String email, String name, String surname, Date birthDate, UserSex sex, UserState state,
                        List<GroupInherited> groupsMember, List<GroupInherited> groupsAdmin, List<ConversationInherited> conversationsWithUser) {
        this.idUser = idUser;
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

    @Id
    private int idUser;
    private List<ConversationInherited> conversationsWithUser;
    private String email;
    private String name;
    private String surname;
    private Date birthDate;
    private UserSex sex;
    private UserState state;
    private List<GroupInherited> groupsMember;
    private List<GroupInherited> groupsAdmin;

}
