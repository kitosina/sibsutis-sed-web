package sibsutis.sed.sedsibsutis.model.dto.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class SignDocumentEncrypt {

    @JsonProperty("encrypt_document")
    private byte[] encryptDocument;

    @JsonProperty("encrypt_secret_key")
    private byte[] encryptSecretKey;

}
