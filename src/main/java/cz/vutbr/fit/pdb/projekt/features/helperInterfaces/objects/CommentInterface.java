package cz.vutbr.fit.pdb.projekt.features.helperInterfaces.objects;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.references.UserReference;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.post.PostTable;

import java.util.Date;

public interface CommentInterface extends ObjectInterface {
    int getId();

    void setId(int id);

    String getText();

    void setText(String text);

    Date getCreatedAt();

    void setCreatedAt(Date createdAt);

    UserReference getUserReference();

    void setUserReference(UserReference userReference);

    PostTable getPostReference();

    void setPostReference(PostTable postReference);
}
