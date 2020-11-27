package cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.inherited;

import cz.vutbr.fit.pdb.projekt.features.persistent.UserReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorInherited implements UserReference {
    private int id;
    private String name;
    private String surname;
}
