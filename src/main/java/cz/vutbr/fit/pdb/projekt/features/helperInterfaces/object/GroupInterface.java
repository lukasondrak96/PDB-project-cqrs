package cz.vutbr.fit.pdb.projekt.features.helperInterfaces.object;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.reference.UserReference;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupState;

public interface GroupInterface extends ObjectInterface {
    int getId();

    void setId(int id);

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    GroupState getState();

    void setState(GroupState state);

    UserReference getUserReference();

    void setUserReference(UserReference userReference);
}
