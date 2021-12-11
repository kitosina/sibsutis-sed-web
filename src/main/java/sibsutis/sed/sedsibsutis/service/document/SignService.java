package sibsutis.sed.sedsibsutis.service.document;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import sibsutis.sed.sedsibsutis.model.dto.crypto.DecryptDocumentInfo;
import sibsutis.sed.sedsibsutis.model.dto.crypto.EncryptDocumentInfo;
import sibsutis.sed.sedsibsutis.model.dto.document.IncomingDocumentEncrypt;
import sibsutis.sed.sedsibsutis.model.dto.document.SendDocumentEncrypt;
import sibsutis.sed.sedsibsutis.model.dto.document.SignDocument;
import sibsutis.sed.sedsibsutis.model.entity.UserSecretEntity;
import sibsutis.sed.sedsibsutis.model.entity.document.SendDocumentEntity;
import sibsutis.sed.sedsibsutis.model.entity.document.SignDocumentEntity;
import sibsutis.sed.sedsibsutis.repository.UserSecretRepository;
import sibsutis.sed.sedsibsutis.repository.document.SendDocumentRepository;
import sibsutis.sed.sedsibsutis.repository.document.SignDocumentRepository;
import sibsutis.sed.sedsibsutis.service.crypto.RSACrypto;
import sibsutis.sed.sedsibsutis.service.pdf.SignImage;
import sibsutis.sed.sedsibsutis.service.security.UserInfoService;

import javax.annotation.PostConstruct;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class SignService {

    private final SignDocumentRepository signDocumentRepository;
    private final UserInfoService userInfoService;
    private final SedDocumentConnector sedDocumentConnector;
    private final UserSecretRepository userSecretRepository;
    private final SendDocumentRepository sendDocumentRepository;
    private final SignImage signImage;

    private RSACrypto rsaCrypto;

    @PostConstruct
    private void initCrypto() {
        rsaCrypto = new RSACrypto();
    }

    @Transactional(rollbackFor = Exception.class)
    public void signContract(SignDocument signDocument) throws Exception {
        String emailSender =  userInfoService.getEmailAuthUser();
        // Получаем документ (зашифрованный)
        IncomingDocumentEncrypt incomingDocumentEncrypt =
                sedDocumentConnector.incomingSedDocumentRequest(signDocument.getDocumentName(), emailSender);
        // Ищем приватный ключ получателя (в данном случае получатель будет отправитель назад)
        UserSecretEntity userSecret = userSecretRepository.findByEmail(userInfoService.getEmailAuthUser())
                .orElseThrow(() -> new RuntimeException("Ошибка поиска ключей"));

        // Дешифруем сообщение
        DecryptDocumentInfo decryptDocumentInfo = rsaCrypto.decryptData(
                incomingDocumentEncrypt.getEncryptDocument(),
                incomingDocumentEncrypt.getEncryptSecretKey(),
                KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(userSecret.getKeyPrivate()))
        );
        // Подписываем документ
        byte[] signTextImg = signImage.createSignTextImg(decryptDocumentInfo.getDecryptDocument(), userSecret.getEmail());

        UserSecretEntity userSecretReceiver = userSecretRepository.findByEmail(signDocument.getEmailReceiver())
                .orElseThrow(() -> new RuntimeException("Ошибка поиска ключей"));

        //Шифруем и отправляем тому кто отправил на подписание
        //Получаем публичный ключ получателя
        PublicKey publicKeyReceiver = KeyFactory
                .getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(userSecretReceiver.getKeyPublic()));

        EncryptDocumentInfo encryptDocumentInfo = rsaCrypto.encryptData(signTextImg, publicKeyReceiver);

        sedDocumentConnector.singSedDocumentRequest(SendDocumentEncrypt.builder()
                .documentName(signDocument.getDocumentName())
                .emailSender(emailSender)
                .encryptDocumentInfo(encryptDocumentInfo)
                .emailReceiver(userSecretReceiver.getEmail())
                .build());
        // Сохраняем документ для отображения без запроса к sed-document
        SignDocumentEntity signDocumentEntity = new SignDocumentEntity();
        signDocumentEntity.setDocumentName(signDocument.getDocumentName());
        signDocumentEntity.setSignFlag(true);
        signDocumentEntity.setEmailReceiver(userSecretReceiver.getEmail());
        signDocumentEntity.setEmailSender(emailSender);
        signDocumentRepository.save(signDocumentEntity);

        log.info("Delete sender {} {} {}", signDocument.getEmailReceiver(), signDocument.getDocumentName(), emailSender);
        // Удаляем из подписи для данного клиента (фактически оно уже подписано), тут надо предумать флаг у отправителя тоже пропадает письмо
        sendDocumentRepository.updateReceiverSign(signDocument.getEmailReceiver(), signDocument.getDocumentName(), emailSender);
    }

    @Transactional(rollbackFor = Exception.class)
    public void noSignContract(SignDocument signDocument) throws Exception {
        String emailSender =  userInfoService.getEmailAuthUser();
        // Получаем документ (зашифрованный)
        IncomingDocumentEncrypt incomingDocumentEncrypt =
                sedDocumentConnector.incomingSedDocumentRequest(signDocument.getDocumentName(), emailSender);
        // Ищем приватный ключ получателя (в данном случае получатель будет отправитель назад)
        UserSecretEntity userSecret = userSecretRepository.findByEmail(userInfoService.getEmailAuthUser())
                .orElseThrow(() -> new RuntimeException("Ошибка поиска ключей"));

        // Дешифруем сообщение
        DecryptDocumentInfo decryptDocumentInfo = rsaCrypto.decryptData(
                incomingDocumentEncrypt.getEncryptDocument(),
                incomingDocumentEncrypt.getEncryptSecretKey(),
                KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(userSecret.getKeyPrivate()))
        );
        // Подписываем(отказываем в подписи) документ
        byte[] signTextImg = signImage.createNoSignTextImg(decryptDocumentInfo.getDecryptDocument(), userSecret.getEmail());

        UserSecretEntity userSecretReceiver = userSecretRepository.findByEmail(signDocument.getEmailReceiver())
                .orElseThrow(() -> new RuntimeException("Ошибка поиска ключей"));

        //Шифруем и отправляем тому кто отправил на подписание
        //Получаем публичный ключ получателя
        PublicKey publicKeyReceiver = KeyFactory
                .getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(userSecretReceiver.getKeyPublic()));

        EncryptDocumentInfo encryptDocumentInfo = rsaCrypto.encryptData(signTextImg, publicKeyReceiver);

        sedDocumentConnector.noSingSedDocumentRequest(SendDocumentEncrypt.builder()
                .documentName(signDocument.getDocumentName())
                .emailSender(emailSender)
                .encryptDocumentInfo(encryptDocumentInfo)
                .emailReceiver(userSecretReceiver.getEmail())
                .build());
        // Сохраняем документ для отображения без запроса к sed-document
        SignDocumentEntity signDocumentEntity = new SignDocumentEntity();
        signDocumentEntity.setDocumentName(signDocument.getDocumentName());
        signDocumentEntity.setSignFlag(false);
        signDocumentEntity.setEmailReceiver(userSecretReceiver.getEmail());
        signDocumentEntity.setEmailSender(emailSender);
        signDocumentRepository.save(signDocumentEntity);

        // Удаляем из подписи для данного клиента (фактически оно уже подписано), тут надо предумать флаг у отправителя тоже пропадает письмо
        sendDocumentRepository.updateReceiverSign(signDocument.getEmailReceiver(), signDocument.getDocumentName(), emailSender);
    }

    public List<SignDocument> getSignDocument(boolean signFlag) {
        List<SignDocumentEntity> signDocumentEntities = signDocumentRepository.findAllByEmailReceiverAndSignFlag(
                userInfoService.getEmailAuthUser(), signFlag
        );
        return convertSignDocumentEntityToSignDocument(signDocumentEntities);
    }

    private List<SignDocument> convertSignDocumentEntityToSignDocument(List<SignDocumentEntity> signDocumentEntities) {
        List<SignDocument> signDocuments = new ArrayList<>();
        for (SignDocumentEntity signDocumentEntity : signDocumentEntities) {
            signDocuments.add(SignDocument.builder()
                    .documentName(signDocumentEntity.getDocumentName())
                    .emailReceiver(signDocumentEntity.getEmailReceiver())
                    .emailSender(signDocumentEntity.getEmailSender())
                    .build());
        }
        return signDocuments;
    }
}
