package sibsutis.sed.sedsibsutis.model.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;

@Builder
@Data
public class Contragent {

    @JsonProperty("email")
    private String email;

    @JsonProperty("address")
    private String address;

    @JsonProperty("university")
    private String university;

    @JsonProperty("faculty")
    private String faculty;

    @JsonProperty("inn")
    private String inn;

    @JsonProperty("fio")
    private String fio;

}
