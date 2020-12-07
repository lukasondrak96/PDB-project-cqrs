package cz.vutbr.fit.pdb.projekt.api.commands.dtos.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewMessageDto {

    @NotBlank(message = "Text zprávy nesmí být prázdný")
    private String text;

    @NotNull(message = "Id odesílatele zprávy musí být zadané")
    private Integer senderId;

    @NotNull(message = "Id příjemce zprávy musí být zadané")
    private Integer recipientId;
}
