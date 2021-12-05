package sibsutis.sed.sedsibsutis.model.dto.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IncomingDocument {
    /**
     * Название документа
     */
    @JsonProperty(value = "document_name")
    private String documentName;

    /**
     * Кто отправил
     */
    @JsonProperty(value = "email_sender")
    private String emailSender;
}
