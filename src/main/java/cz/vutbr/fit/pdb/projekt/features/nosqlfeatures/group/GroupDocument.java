package cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group;

import com.fasterxml.jackson.annotation.JsonInclude;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.objects.GroupInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentGroup;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.references.UserReference;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.inherited.CreatorInherited;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.inherited.MemberInherited;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.inherited.PostInherited;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupState;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@Document
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupDocument implements GroupInterface, PersistentGroup {

    private CreatorInherited creator;

    public GroupDocument(String name, String description, GroupState state, CreatorInherited creator, List<PostInherited> posts, List<MemberInherited> members) {
        this.name = name;
        this.description = description;
        this.state = state;
        this.creator = creator;
        this.posts = posts;
        this.members = members;
    }

    @Id
    private int id;
    private String name;
    private String description;
    private GroupState state;
    private List<PostInherited> posts;
    private List<MemberInherited> members;

    @Override
    public UserReference getUserReference() {
        return creator;
    }

    @Override
    public void setUserReference(UserReference userReference) {
        UserTable userTable = (UserTable) userReference;
        creator = new CreatorInherited(userTable.getId(), userTable.getName(), userTable.getSurname());
    }
}
