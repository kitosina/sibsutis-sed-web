package sibsutis.sed.sedsibsutis.service.crypto;

import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;

public abstract class Crypto {

    public abstract KeyPair generateKeyPair() throws GeneralSecurityException;

    public abstract ContentSigner getContentSigner(PrivateKey privateKey) throws OperatorCreationException;
}
