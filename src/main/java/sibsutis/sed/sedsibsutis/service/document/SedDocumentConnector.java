package sibsutis.sed.sedsibsutis.service.document;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import sibsutis.sed.sedsibsutis.model.dto.document.IncomingDocumentEncrypt;
import sibsutis.sed.sedsibsutis.model.dto.document.SendDocumentEncrypt;
import sibsutis.sed.sedsibsutis.model.entity.document.SendDocumentEntity;

@RequiredArgsConstructor
@Service
public class SedDocumentConnector {

    @Value("${sed.document.send.url}")
    private String sendUrl;

    @Value("${sed.document.sign.url}")
    private String singUrl;

    @Value("${sed.document.no.sign.url}")
    private String noSingUrl;

    private final WebClient webClient;

    public void sendSedDocumentRequest(final SendDocumentEncrypt sendDocument) {
        webClient.post()
                .uri(sendUrl)
                .bodyValue(sendDocument)
                .exchange()
                .block();
    }

    public void singSedDocumentRequest(final SendDocumentEncrypt sendDocument) {
        webClient.post()
                .uri(singUrl)
                .bodyValue(sendDocument)
                .exchange()
                .block();
    }

    public void noSingSedDocumentRequest(final SendDocumentEncrypt sendDocument) {
        webClient.post()
                .uri(noSingUrl)
                .bodyValue(sendDocument)
                .exchange()
                .block();
    }

    public IncomingDocumentEncrypt incomingSedDocumentRequest(String documentName, String emailReceiver) {
        ClientResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/document/incoming")
                        .queryParam("document_name", documentName)
                        .queryParam("email_receiver", emailReceiver)
                        .build())
                .exchange()
                .block();
        return response.bodyToMono(IncomingDocumentEncrypt.class).block();
    }
}
