package cz.vutbr.fit.pdb.projekt.nosqlfeatures.user;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.List;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDocument {

    public UserDocument(String name, String surname, int age, String sex, String state, List<GroupInheritted> groupsMember,
                        List<GroupInheritted> groupsAdmin, List<ConversationInheritted> conversations_with_user) {
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
    private int id;
    private String name;
    private String surname;
    private int age;
    private String sex;
    private String state;
    private List<GroupInheritted> groupsMember;
    private List<GroupInheritted> groupsAdmin;
    private List<ConversationInheritted> conversations_with_user;
}
