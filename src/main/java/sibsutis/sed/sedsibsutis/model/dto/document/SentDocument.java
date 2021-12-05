package sibsutis.sed.sedsibsutis.model.dto.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SentDocument {
    /**
     * Название документа
     */
    @JsonProperty(value = "document_name")
    private String documentName;

    /**
     * Кому отправили
     */
    @JsonProperty(value = "email_receiver")
    private String emailReceiver;
}
