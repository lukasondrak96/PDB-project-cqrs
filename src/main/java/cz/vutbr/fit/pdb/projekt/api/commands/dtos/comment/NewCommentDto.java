package cz.vutbr.fit.pdb.projekt.api.commands.dtos.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentDto {

    @NotBlank(message = "Komentář nesmí být prázdný")
    private String text;

    @NotNull(message = "Id autora příspěvku musí být zadané")
    private Integer creatorId;

    @NotNull(message = "Id příspěvku, ke kterému je komentář vkládán, je povinné")
    private Integer postId;
}
