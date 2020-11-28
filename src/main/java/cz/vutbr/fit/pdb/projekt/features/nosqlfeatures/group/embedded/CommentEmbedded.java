package cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.objects.CommentInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.persistent.PersistentComment;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.references.UserReference;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.post.PostTable;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentEmbedded implements CommentInterface, PersistentComment {
    private int id;
    private String text;
    private Date createdAt;
    private CreatorEmbedded creator;

    @Transient
    private PostTable postReference;

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
