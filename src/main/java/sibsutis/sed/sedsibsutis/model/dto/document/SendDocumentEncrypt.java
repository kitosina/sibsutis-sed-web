package sibsutis.sed.sedsibsutis.model.dto.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import sibsutis.sed.sedsibsutis.model.dto.crypto.EncryptDocumentInfo;

@Data
@Builder
public class SendDocumentEncrypt {

    @JsonProperty("document_name")
    private String documentName;

    @JsonProperty("email_sender")
    private String emailSender;

    @JsonProperty("email_receiver")
    private String emailReceiver;

    @JsonProperty("encrypt_document_info")
    private EncryptDocumentInfo encryptDocumentInfo;
}
