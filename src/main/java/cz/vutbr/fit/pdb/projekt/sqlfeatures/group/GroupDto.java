package cz.vutbr.fit.pdb.projekt.sqlfeatures.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {
    private String name;

    private String description;

    private String state;
}
