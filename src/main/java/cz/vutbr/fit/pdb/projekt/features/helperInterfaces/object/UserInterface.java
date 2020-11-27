package cz.vutbr.fit.pdb.projekt.features.helperInterfaces.object;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.ObjectInterface;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserSex;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserState;

import java.util.Date;

public interface UserInterface extends ObjectInterface {
    int getId();

    void setId(int id);

    String getEmail();

    void setEmail(String email);

    String getName();

    void setName(String name);

    String getSurname();

    void setSurname(String surname);

    Date getBirthDate();

    void setBirthDate(Date birthDate);

    UserSex getSex();

    void setSex(UserSex sex);

    UserState getState();

    void setState(UserState state);
}
