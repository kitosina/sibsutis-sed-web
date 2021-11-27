package sibsutis.sed.sedsibsutis.service.crypto;

import org.bouncycastle.asn1.cryptopro.ECGOST3410NamedCurves;
import org.bouncycastle.asn1.cryptopro.ECGOST3410ParamSetParameters;
import org.bouncycastle.jce.ECGOST3410NamedCurveTable;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.stereotype.Component;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;

/**
 * Шифратор и дешифратор документов по ГОСТ-3410
 */
public class GOSTCrypto extends Crypto {

    /**
     * Генератор пары ключей (public key, private key)
     * @return KeyPair - пара ключей для пользователя в системе
     * @throws GeneralSecurityException
     * @see KeyPair
     */
    @Override
    public KeyPair generateKeyPair() throws GeneralSecurityException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECGOST3410");
        keyPairGenerator.initialize(ECGOST3410NamedCurveTable.getParameterSpec("GostR3410-2001-CryptoPro-A"));
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * Метод для подписи документов приватным ключом
     * @param privateKey
     * @return
     * @throws OperatorCreationException
     */
    @Override
    public ContentSigner getContentSigner(PrivateKey privateKey) throws OperatorCreationException {
        return new JcaContentSignerBuilder("GOST3411withECGOST3410").build(privateKey);
    }

}
