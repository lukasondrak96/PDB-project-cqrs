package cz.vutbr.fit.pdb.projekt.nosqlfeatures.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorInherited {
    private String name;
    private String surname;
}
