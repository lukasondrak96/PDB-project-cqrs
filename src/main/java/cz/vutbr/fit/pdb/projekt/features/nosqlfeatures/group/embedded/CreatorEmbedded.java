package cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded;

import cz.vutbr.fit.pdb.projekt.features.helperInterfaces.references.UserReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatorEmbedded implements UserReference {
    private int id;
    private String name;
    private String surname;
}
