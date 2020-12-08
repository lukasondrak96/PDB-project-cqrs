package cz.vutbr.fit.pdb.projekt.features.helperInterfaces.objects;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.references.UserReference;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupTable;

import java.util.Date;

public interface PostInterface extends ObjectInterface {
    int getId();

    void setId(int id);

    String getTitle();

    void setTitle(String title);

    String getText();

    void setText(String text);

    Date getCreatedAt();

    void setCreatedAt(Date createdAt);

    UserReference getCreator();

    void setCreator(UserReference userReference);

    GroupTable getGroupReference();

    void setGroupReference(GroupTable groupReference);
}
