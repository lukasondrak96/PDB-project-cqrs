package cz.vutbr.fit.pdb.projekt.api.commands.dtos.group;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class UpdateGroupDto {

    @NotBlank(message = "Jméno musí být zadané")
    private String name;

    private String description;

    public UpdateGroupDto(@NotBlank(message = "Jméno musí být zadané") String name,
                          String description) {
        this.name = name;
        this.description = description;
    }
}
