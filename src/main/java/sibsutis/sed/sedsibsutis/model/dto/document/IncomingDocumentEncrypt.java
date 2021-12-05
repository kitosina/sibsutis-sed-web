package sibsutis.sed.sedsibsutis.model.dto.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IncomingDocumentEncrypt {

    @JsonProperty("encrypt_document")
    private byte[] encryptDocument;

    @JsonProperty("encrypt_secret_key")
    private byte[] encryptSecretKey;

}
