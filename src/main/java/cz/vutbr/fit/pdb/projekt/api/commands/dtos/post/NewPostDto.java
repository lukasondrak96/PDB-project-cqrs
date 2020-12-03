package cz.vutbr.fit.pdb.projekt.api.commands.dtos.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPostDto {

    @NotBlank(message = "Titulek příspěvku musí být zadán")
    private String title;

    @NotBlank(message = "Příspěvek nesmí být prázdný")
    private String text;

    @NotNull(message = "Id autora příspěvku musí být zadané")
    private Integer creatorId;

    @NotNull(message = "Id skupiny, do které je příspěvek vkládán, je povinné")
    private Integer groupId;

}
