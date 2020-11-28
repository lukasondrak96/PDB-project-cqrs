package cz.vutbr.fit.pdb.projekt.features.helperInterfaces.objects;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupTable;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserTable;

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

    GroupTable getGroupTableReference();

    void setGroupTableReference(GroupTable groupTableReference);

    UserTable getUserTableReference();

    void setUserTableReference(UserTable userTableReference);
}
