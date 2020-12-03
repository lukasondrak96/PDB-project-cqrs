package cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserSex {

    @JsonProperty("male") MALE,
    @JsonProperty("female") FEMALE,
    @JsonProperty("not specified") NOT_SPECIFIED

}
