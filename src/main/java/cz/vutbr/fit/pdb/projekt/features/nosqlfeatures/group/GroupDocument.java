package cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group;

import com.fasterxml.jackson.annotation.JsonInclude;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.objects.GroupInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentGroup;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.references.UserReference;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.CreatorEmbedded;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.MemberEmbedded;
import cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded.PostEmbedded;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupState;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Document
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupDocument implements GroupInterface, PersistentGroup {

    private CreatorEmbedded creator;

    public GroupDocument(String name, String description, GroupState state, CreatorEmbedded creator, List<PostEmbedded> posts, List<MemberEmbedded> members) {
        this.name = name;
        this.description = description;
        this.state = state;
        this.creator = creator;
        this.posts = posts;
        this.members = members;
    }

    public GroupDocument(int id, String name, String description, GroupState state, CreatorEmbedded creator, List<PostEmbedded> posts, List<MemberEmbedded> members) {
        this.id = id;
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
    private List<PostEmbedded> posts = new ArrayList<>();
    private List<MemberEmbedded> members = new ArrayList<>();

    @Override
    public UserReference getCreator() {
        return creator;
    }

    @Override
    public void setCreator(UserReference userReference) {
        creator = new CreatorEmbedded(userReference.getId(), userReference.getName(), userReference.getSurname());
    }
}
