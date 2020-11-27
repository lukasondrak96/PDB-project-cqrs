package cz.vutbr.fit.pdb.projekt.features.persistent;

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
}
