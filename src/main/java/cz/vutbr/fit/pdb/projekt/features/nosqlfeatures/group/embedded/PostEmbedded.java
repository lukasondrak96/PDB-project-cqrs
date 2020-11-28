package cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.objects.PostInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentPost;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.references.UserReference;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupTable;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostEmbedded implements PostInterface, PersistentPost {
    private int id;
    private String title;
    private String text;
    private Date createdAt;
    private CreatorEmbedded creator;
    private List<CommentEmbedded> comments;

    @Transient
    private GroupTable groupReference;

    @Override
    public UserReference getUserReference() {
        return creator;
    }

    @Override
    public void setUserReference(UserReference userReference) {
        UserTable userTable = (UserTable) userReference;
        creator = new CreatorEmbedded(userTable.getId(), userTable.getName(), userTable.getSurname());
    }
}
