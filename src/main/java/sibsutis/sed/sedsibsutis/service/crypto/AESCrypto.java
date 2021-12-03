package sibsutis.sed.sedsibsutis.service.crypto;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESCrypto {

    @SneakyThrows
    public SecretKey generateSecreteKeyAes() {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // for example
        SecretKey secretKey = keyGen.generateKey();
        return secretKey;
    }

    @SneakyThrows
    public byte[] encryptDataAes(byte[] document, SecretKey keyAes) {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keyAes);
        byte[] decStr = cipher.doFinal(document);
        return decStr;
    }

    @SneakyThrows
    public byte[] decryptDataAes(byte[] document, byte[] decryptSecretKeyAes) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(decryptSecretKeyAes, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decStr = cipher.doFinal(document);
        return decStr;
    }

}
