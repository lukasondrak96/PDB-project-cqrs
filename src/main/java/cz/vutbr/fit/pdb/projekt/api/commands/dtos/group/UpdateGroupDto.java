package cz.vutbr.fit.pdb.projekt.api.commands.dtos.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGroupDto {

    @NotBlank(message = "Jméno musí být zadané")
    private String name;

    private String description;

}
