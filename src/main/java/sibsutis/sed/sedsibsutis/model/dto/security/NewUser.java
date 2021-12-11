package sibsutis.sed.sedsibsutis.model.dto.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class NewUser {
    /**
     * Сообщение о неверном формате email
     */
    private static final String ERROR_FORMAT_EMAIL = "Email error format";
    /**
     * Сообщение о пустом формате
     */
    private static final String ERROR_EMAIL_NOT_EMPTY = "Email not may be empty";
    /**
     * Сообщение о неверном формате password
     */
    private static final String ERROR_PASSWORD_NOT_EMPTY = "Password not may be empty";

    @Email(message = ERROR_FORMAT_EMAIL)
    @NotBlank(message = ERROR_EMAIL_NOT_EMPTY)
    @JsonProperty("email")
    private String email;

    @NotBlank(message = ERROR_PASSWORD_NOT_EMPTY)
    @JsonProperty("password")
    private String password;

    @JsonProperty("contragent")
    private ContragentInfo contragentInfo;

    @Data
    public static class ContragentInfo {

        @JsonProperty("address")
        private String address;

        @JsonProperty("university")
        private String university;

        @JsonProperty("faculty")
        private String faculty;

        @JsonProperty("INN")
        private String inn;

        @JsonProperty("FIO")
        private String fio;
    }
}
