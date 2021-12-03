package sibsutis.sed.sedsibsutis.model.dto.crypto;

import lombok.Builder;
import lombok.Data;

import javax.crypto.SecretKey;

@Data
@Builder
public class EncryptDocumentInfo {

    private byte[] encryptDocument;

    private byte[] encryptSecretKey;

}
