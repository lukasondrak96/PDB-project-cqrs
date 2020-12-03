package cz.vutbr.fit.pdb.projekt.api.commands.dtos.user;

import cz.vutbr.fit.pdb.projekt.features.sqlfeatures.user.UserSex;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UpdateUserDto extends NewUserDto {

    public UpdateUserDto(@NotBlank(message = "Email musí být zadaný") String email,
                         @NotBlank(message = "Jméno musí být zadané") String name,
                         @NotBlank(message = "Příjmení musí být zadané") String surname,
                         @NotNull(message = "Datum narození musí být zadané") Date birthDate,
                         @NotNull(message = "Pohlaví musí být zadané") UserSex sex) {
        super(email, name, surname, birthDate, sex);
    }

}
