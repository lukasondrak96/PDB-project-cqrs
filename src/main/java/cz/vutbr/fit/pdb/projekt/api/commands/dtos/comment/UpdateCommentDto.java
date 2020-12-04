package cz.vutbr.fit.pdb.projekt.api.commands.dtos.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentDto {

    @NotBlank(message = "Komentář nesmí být prázdný")
    private String text;

}
