package cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user;

import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.inherited.ConversationInherited;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.inherited.GroupInherited;
import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentUser;
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
public class UserDocument implements PersistentUser {

    public UserDocument(String email, String name, String surname, Date birthDate, UserSex sex, List<GroupInherited> groupsMember,
                        List<GroupInherited> groupsAdmin, List<ConversationInherited> conversations_with_user) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.sex = sex;
        this.state = UserState.ACTIVATED;
        this.groupsMember = groupsMember;
        this.groupsAdmin = groupsAdmin;
        this.conversations_with_user = conversations_with_user;
    }

    @Id
    private String id;
    private String email;
    private String name;
    private String surname;
    private Date birthDate;
    private UserSex sex;
    private UserState state;
    private List<GroupInherited> groupsMember;
    private List<GroupInherited> groupsAdmin;
    private List<ConversationInherited> conversations_with_user;
}
