package sibsutis.sed.sedsibsutis.model.dto.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class NewUser {

    private static final String ERROR_FORMAT_EMAIL = "Email error format";
    private static final String ERROR_EMAIL_NOT_EMPTY = "Email not may be empty";

    private static final String ERROR_PASSWORD_NOT_EMPTY = "Password not may be empty";

    @Email(message = ERROR_FORMAT_EMAIL)
    @NotBlank(message = ERROR_EMAIL_NOT_EMPTY)
    @JsonProperty("email")
    private String email;

    @NotBlank(message = ERROR_PASSWORD_NOT_EMPTY)
    @JsonProperty("password")
    private String password;

}
