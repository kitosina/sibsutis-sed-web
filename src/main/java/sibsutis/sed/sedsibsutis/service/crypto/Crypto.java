package sibsutis.sed.sedsibsutis.service.crypto;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;

@Slf4j
public abstract class Crypto {

    public abstract KeyPair generateKeyPair() throws GeneralSecurityException;

    public abstract ContentSigner getContentSigner(PrivateKey privateKey) throws OperatorCreationException;

    public byte[] getByteSignerDocument(String document, PrivateKey privateKey) throws OperatorCreationException, IOException {
        ContentSigner contentSigner = getContentSigner(privateKey);
        contentSigner.getOutputStream().write(document.getBytes());
        return contentSigner.getSignature();
    }
}
