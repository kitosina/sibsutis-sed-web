package sibsutis.sed.sedsibsutis.service.crypto;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sibsutis.sed.sedsibsutis.model.dto.crypto.DecryptDocumentInfo;
import sibsutis.sed.sedsibsutis.model.dto.crypto.EncryptDocumentInfo;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

/**
 * Шифратор и дешифратор документов по ГОСТ-3410
 */
@NoArgsConstructor
@Slf4j
public class RSACrypto extends AESCrypto {


    @SneakyThrows
    public KeyPair generateKeyPair() {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = SecureRandom.getInstanceStrong();
        generator.initialize(2048, random);
        KeyPair keyPair = generator.generateKeyPair();
        return keyPair;
    }

    @SneakyThrows
    public DecryptDocumentInfo decryptData(byte[] document, byte[] aesKey, PrivateKey privateKey)  {

        // Дешифруем AES через RSA - приватным ключом получателя
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptSecretKey = cipher.doFinal(aesKey);

        byte[] decryptDocumentAes = decryptDataAes(document, decryptSecretKey);
        return DecryptDocumentInfo.builder()
                .decryptDocument(decryptDocumentAes)
                .build();
    }

    @SneakyThrows
    public EncryptDocumentInfo encryptData(byte[] document, PublicKey publicKey) {
        SecretKey aesKey = generateSecreteKeyAes();
        byte[] encryptDocumentAes = encryptDataAes(document, aesKey);

        // Шифруем AES через RSA - публичным ключом получателя
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptSecretKey = cipher.doFinal(aesKey.getEncoded());

        return EncryptDocumentInfo.builder()
                .encryptDocument(encryptDocumentAes)
                .encryptSecretKey(encryptSecretKey)
                .build();
    }

}
