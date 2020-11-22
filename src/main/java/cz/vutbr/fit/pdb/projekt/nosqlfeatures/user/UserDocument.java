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
//    @Id
//    @SequenceGenerator(name = "PostIdGenerator", sequenceName = "POST_SEQUENCE", allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PostIdGenerator")
//    private int idPost;
//
//    private String title;
//
//    private String text;
//
//    private Date createdAt;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "idGroup", referencedColumnName = "idGroup")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private GroupTable groupTableReference;


    public UserDocument(String name, String surname, List<GroupInheritted> groupsMember, List<GroupInheritted> groupsAdmin) {
        this.name = name;
        this.surname = surname;
        this.groupsMember = groupsMember;
        this.groupsAdmin = groupsAdmin;
    }

    @Id
    private int id;
    private String name;
    private String surname;
//    private int age;
//    private String sex;
//    private String state;
    private List<GroupInheritted> groupsMember;
    private List<GroupInheritted> groupsAdmin;
//    private List<ConversationInheritted> conversations_with_user;
}
