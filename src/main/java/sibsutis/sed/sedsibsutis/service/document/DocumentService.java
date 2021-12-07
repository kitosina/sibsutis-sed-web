package sibsutis.sed.sedsibsutis.service.document;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sibsutis.sed.sedsibsutis.model.dto.crypto.DecryptDocumentInfo;
import sibsutis.sed.sedsibsutis.model.dto.crypto.EncryptDocumentInfo;
import sibsutis.sed.sedsibsutis.model.dto.document.IncomingDocument;
import sibsutis.sed.sedsibsutis.model.dto.document.IncomingDocumentEncrypt;
import sibsutis.sed.sedsibsutis.model.dto.document.SentDocument;
import sibsutis.sed.sedsibsutis.model.dto.document.SendDocumentEncrypt;
import sibsutis.sed.sedsibsutis.model.entity.UserSecretEntity;
import sibsutis.sed.sedsibsutis.model.entity.document.SendDocumentEntity;
import sibsutis.sed.sedsibsutis.repository.UserSecretRepository;
import sibsutis.sed.sedsibsutis.repository.document.SendDocumentRepository;
import sibsutis.sed.sedsibsutis.repository.document.SignDocumentRepository;
import sibsutis.sed.sedsibsutis.service.crypto.RSACrypto;
import sibsutis.sed.sedsibsutis.service.pdf.SignImage;
import sibsutis.sed.sedsibsutis.service.security.UserInfoService;

import javax.annotation.PostConstruct;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentService {

    private final UserInfoService userInfoService;
    private final SendDocumentRepository sendDocumentRepository;
    private final SedDocumentConnector sedDocumentConnector;
    private final UserSecretRepository userSecretRepository;

    private RSACrypto rsaCrypto;

    @PostConstruct
    private void initCrypto() {
        rsaCrypto = new RSACrypto();
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public void sendDocument(final String documentName, final String emailReceiver, byte[] document) {
        // Проверим что документ с одним и тем же названием нет
        SendDocumentEntity documentSentEmailSenderAndDocumentName
                = sendDocumentRepository.findAllByEmailSenderAndDocumentName(
                        userInfoService.getEmailAuthUser(), documentName);
        if (documentSentEmailSenderAndDocumentName != null) {
            throw new RuntimeException("Документ с таким названием уже есть для пользователя"
                    + userInfoService.getEmailAuthUser());
        }


        // Сохраняем информацию об отправителе и получателе, а также название файла для дальнейшего отображения
        // на стороне frontend (отправленные/входящие)
        SendDocumentEntity sendDocumentEntity = createSendDocumentEntity(documentName, emailReceiver, document);
        sendDocumentRepository.save(sendDocumentEntity);

        // Шифруем документ публичным ключем получателя
        UserSecretEntity userSecret = userSecretRepository.findByEmail(emailReceiver)
                .orElseThrow(() -> new RuntimeException("Не найдены секретные ключи пользователя"));
        PublicKey publicKeyReceiver = KeyFactory
                .getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(userSecret.getKeyPublic()));

        EncryptDocumentInfo encryptDocumentInfo = rsaCrypto.encryptData(document, publicKeyReceiver);

        sedDocumentConnector.sendSedDocumentRequest(SendDocumentEncrypt.builder()
                .documentName(documentName)
                .emailSender(sendDocumentEntity.getEmailSender())
                .encryptDocumentInfo(encryptDocumentInfo)
                .emailReceiver(emailReceiver)
                .build());

    }

    public List<SentDocument> sentDocumentUser() {
        List<SendDocumentEntity> allSenderDocument =
                sendDocumentRepository.findAllByEmailSender(userInfoService.getEmailAuthUser());
        return convertSendDocumentEntityToSentDocument(allSenderDocument);
    }

    public byte[] sentDocumentUserFile(final String documentName) {
        String emailSender =  userInfoService.getEmailAuthUser();
        SendDocumentEntity documentSentEmailSenderAndDocumentName
                = sendDocumentRepository.findAllByEmailSenderAndDocumentName(emailSender, documentName);
        return documentSentEmailSenderAndDocumentName.getDocument();
    }

    public List<IncomingDocument> incomingDocumentUser() {
        List<SendDocumentEntity> allIncomingDocument =
                sendDocumentRepository.findAllByEmailReceiver(userInfoService.getEmailAuthUser());
        return convertSendDocumentEntityToIncomingDocument(allIncomingDocument);
    }

    @SneakyThrows
    public byte[] incomingDocumentUserFile(String documentName) {
        String emailReceiver =  userInfoService.getEmailAuthUser();
        // Получаем документ (зашифрованный)
        IncomingDocumentEncrypt incomingDocumentEncrypt =
                sedDocumentConnector.incomingSedDocumentRequest(documentName, emailReceiver);
        // Ищем приватный ключ получателя
        UserSecretEntity userSecret = userSecretRepository.findByEmail(userInfoService.getEmailAuthUser())
                .orElseThrow(() -> new RuntimeException("Ошибка поиска ключей"));

        // Дешифруем сообщение
        DecryptDocumentInfo decryptDocumentInfo = rsaCrypto.decryptData(
                incomingDocumentEncrypt.getEncryptDocument(),
                incomingDocumentEncrypt.getEncryptSecretKey(),
                KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(userSecret.getKeyPrivate()))
        );
        return decryptDocumentInfo.getDecryptDocument();
    }

    private SendDocumentEntity createSendDocumentEntity(
            final String documentName, final String emailReceiver, byte[] document) {
        SendDocumentEntity sendDocumentEntity = new SendDocumentEntity();
        sendDocumentEntity.setDocumentName(documentName);
        sendDocumentEntity.setEmailSender(userInfoService.getEmailAuthUser());
        sendDocumentEntity.setEmailReceiver(emailReceiver);
        sendDocumentEntity.setDocument(document);
        return sendDocumentEntity;
    }

    private List<SentDocument> convertSendDocumentEntityToSentDocument(
            final List<SendDocumentEntity> sendDocumentEntities) {
        List<SentDocument> sentDocuments = new ArrayList<>();
        for (SendDocumentEntity sendDocumentEntity : sendDocumentEntities) {
            sentDocuments.add(SentDocument.builder()
                    .documentName(sendDocumentEntity.getDocumentName())
                    .emailReceiver(sendDocumentEntity.getEmailReceiver())
                    .build());
        }
        return sentDocuments;
    }

    private List<IncomingDocument> convertSendDocumentEntityToIncomingDocument(
            final List<SendDocumentEntity> sendDocumentEntities) {
        List<IncomingDocument> incomingDocuments = new ArrayList<>();
        for (SendDocumentEntity sendDocumentEntity : sendDocumentEntities) {
            incomingDocuments.add(IncomingDocument.builder()
                    .documentName(sendDocumentEntity.getDocumentName())
                    .emailSender(sendDocumentEntity.getEmailSender())
                    .build());
        }
        return incomingDocuments;
    }
}
