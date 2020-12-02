package cz.vutbr.fit.pdb.projekt.features.nosqlfeatures.group.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberEmbedded {
    private int id;
    private String name;
    private String surname;
}
