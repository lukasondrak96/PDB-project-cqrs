package cz.vutbr.fit.pdb.projekt.api.commands.dtos.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserSex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserDto {

    @NotBlank(message = "Email musí být zadaný")
    private String email;

    @NotBlank(message = "Jméno musí být zadané")
    private String name;

    @NotBlank(message = "Příjmení musí být zadané")
    private String surname;

    @NotNull(message = "Datum narození musí být zadané")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date birthDate;

    @NotNull(message = "Pohlaví musí být zadané")
    private UserSex sex;

}
