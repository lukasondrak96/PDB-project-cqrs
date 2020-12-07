package cz.vutbr.fit.pdb.projekt.features.helperInterfaces.objects;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.references.UserReference;

import java.util.Date;

public interface MessageInterface extends ObjectInterface {
    int getId();

    void setId(int id);

    String getText();

    void setText(String text);

    Date getCreatedAt();

    void setCreatedAt(Date createdAt);

    Date getReadAt();

    void setReadAt(Date readAt);

    UserReference getSender();

    void setSender(UserReference sender);

    UserReference getRecipient();

    void setRecipient(UserReference recipient);
}
