package cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user;

import cz.vutbr.fit.pdb.projekt.features.persistent.PersistentUser;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.inherited.ConversationInherited;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.user.inherited.GroupInherited;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.List;

@Document
@Data
@NoArgsConstructor
public class UserDocument implements PersistentUser {

    public UserDocument(String name, String surname, int age, String sex, String state, List<GroupInherited> groupsMember,
                        List<GroupInherited> groupsAdmin, List<ConversationInherited> conversations_with_user) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.sex = sex;
        this.state = state;
        this.groupsMember = groupsMember;
        this.groupsAdmin = groupsAdmin;
        this.conversations_with_user = conversations_with_user;
    }

    @Id
    private String id;
    private String name;
    private String surname;
    private int age;
    private String sex;
    private String state;
    private List<GroupInherited> groupsMember;
    private List<GroupInherited> groupsAdmin;
    private List<ConversationInherited> conversations_with_user;
}
