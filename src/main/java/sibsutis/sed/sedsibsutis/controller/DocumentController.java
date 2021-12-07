package sibsutis.sed.sedsibsutis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sibsutis.sed.sedsibsutis.service.document.DocumentService;

import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;

import static sibsutis.sed.sedsibsutis.controller.DocumentController.DOCUMENT_URL;

@RequestMapping(DOCUMENT_URL)
@RestController
public class DocumentController {

    public static final String DOCUMENT_URL = "/document";
    private static final String DOCUMENT_SEND_URL = "/send";
    private static final String DOCUMENT_SENT_URL = "/sent";
    private static final String DOCUMENT_INCOMING_URL = "/incoming";
    private static final String DOCUMENT_SIGN_URL = "/sign";

    /**
     * URL для получения списка отправленных документов для auth user-а
     */
    private static final String DOCUMENT_ALL_SENT_USER_URL = "/sent/message";

    /**
     * URL для получения списка входящих документов для auth user-а
     */
    private static final String DOCUMENT_ALL_INCOMING_USER_URL = "/incoming/message";

    @Autowired
    private DocumentService documentService;

    @PostMapping(DOCUMENT_SEND_URL)
    public ResponseEntity sendDocument(
            @RequestParam("document") MultipartFile documentSend,
            @RequestParam("email_receiver") String emailReceiver) throws IOException {
        documentService.sendDocument(documentSend.getOriginalFilename(), emailReceiver, documentSend.getBytes());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Скачать pdf документ (отправленные)
     * @param documentName
     * @return
     * @throws SQLException
     */
    @GetMapping(DOCUMENT_SENT_URL)
    private ResponseEntity sentDocumentUserFile(@RequestParam("document_name") String documentName) throws SQLException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set("Content-Disposition", "inline");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        headers.setContentDispositionFormData(documentName, documentName);
        return new ResponseEntity<>(documentService.sentDocumentUserFile(documentName), headers, HttpStatus.OK);
    }

    /**
     * Скачать pdf документ (входящие)
     * @param documentName
     * @return
     * @throws SQLException
     */
    @GetMapping(DOCUMENT_INCOMING_URL)
    private ResponseEntity incomingDocumentFile(@RequestParam("document_name") String documentName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set("Content-Disposition", "inline");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        headers.setContentDispositionFormData(documentName, documentName);
        return new ResponseEntity<>(
                documentService.incomingDocumentUserFile(documentName), headers, HttpStatus.OK);
    }

    /**
     * URL для получения документов с подписью (отказ и подписанно)
     * @return
     */
    @GetMapping(DOCUMENT_SIGN_URL)
    public ResponseEntity signDocumentUser(@RequestParam("document_name") String documentName,
                                           @RequestParam("sign_flag") boolean signFlag) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set("Content-Disposition", "inline");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        headers.setContentDispositionFormData(documentName, documentName);
        return new ResponseEntity<>(
                documentService.signDocumentUser(documentName, signFlag), headers, HttpStatus.OK);
    }

    /**
     * Получение всех отправленных сообщений (список)
     * @return
     */
    @GetMapping(DOCUMENT_ALL_SENT_USER_URL)
    public ResponseEntity sentDocumentUser() {
        return ResponseEntity.ok(documentService.sentDocumentUser());
    }

    /**
     * Получение всех входящих сообщений (список)
     * @return
     */
    @GetMapping(DOCUMENT_ALL_INCOMING_USER_URL)
    public ResponseEntity incomingDocumentUser() {
        return ResponseEntity.ok(documentService.incomingDocumentUser());
    }
}
