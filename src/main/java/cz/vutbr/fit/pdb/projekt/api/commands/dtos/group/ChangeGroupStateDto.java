package cz.vutbr.fit.pdb.projekt.api.commands.dtos.group;


import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.group.GroupState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeGroupStateDto {

    @NotNull(message = "Stav musí být zadán")
    private GroupState state;

}
