package cz.vutbr.fit.pdb.projekt.api.commands.dtos.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostDto {

    @NotBlank(message = "Titulek příspěvku musí být zadán")
    private String title;

    @NotBlank(message = "Příspěvek nesmí být prázdný")
    private String text;

}
