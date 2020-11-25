package cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum GroupState {
    @JsonProperty("public") PUBLIC("public"),
    @JsonProperty("private") PRIVATE("private"),
    @JsonProperty("archived") ARCHIVED("archived"),
    ;

    GroupState(String name) {
    }
}
