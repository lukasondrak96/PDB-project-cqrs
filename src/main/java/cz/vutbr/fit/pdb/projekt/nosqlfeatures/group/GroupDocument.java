package cz.vutbr.fit.pdb.projekt.nosqlfeatures.group;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@Document
@Data
@NoArgsConstructor
public class GroupDocument {

    public GroupDocument(String name, String description, String state, AuthorInherited author, List<PostInherited> posts, List<MemberInherited> members) {
        this.name = name;
        this.description = description;
        this.state = state;
        this.author = author;
        this.posts = posts;
        this.members = members;
    }

    @Id
    private int id;
    private String name;
    private String description;
    private String state;
    private AuthorInherited author;
    private List<PostInherited> posts;
    private List<MemberInherited> members;
}
