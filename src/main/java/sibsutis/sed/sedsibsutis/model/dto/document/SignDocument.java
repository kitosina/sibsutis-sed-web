package sibsutis.sed.sedsibsutis.model.dto.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SignDocument {
    @JsonProperty("document_name")
    private String documentName;

    @JsonProperty("email_receiver")
    private String emailReceiver;

    @JsonProperty("email_sender")
    private String emailSender;
}
