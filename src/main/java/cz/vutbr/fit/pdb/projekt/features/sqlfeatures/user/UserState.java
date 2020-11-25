package cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserState {
    @JsonProperty("activated") ACTIVATED,
    @JsonProperty("deactivated") DEACTIVATED
}
