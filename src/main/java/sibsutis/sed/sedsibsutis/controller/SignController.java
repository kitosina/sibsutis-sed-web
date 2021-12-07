package sibsutis.sed.sedsibsutis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sibsutis.sed.sedsibsutis.model.dto.document.SignDocument;
import sibsutis.sed.sedsibsutis.service.document.SignService;

@RequestMapping("/sign")
@RestController
public class SignController {

    @Autowired
    private SignService service;

    @PostMapping("/contract")
    public ResponseEntity signContract(@RequestBody SignDocument signDocument) throws Exception {
        service.signContract(signDocument);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/no/contract")
    public ResponseEntity noSignContract(@RequestBody SignDocument signDocument) throws Exception {
        service.noSignContract(signDocument);
        return ResponseEntity.ok().build();
    }

    /**
     * Список всех подписанных документов
     * 1 - подписан, 0 - отказ в подписи
     */
    @GetMapping("/document")
    public ResponseEntity getSignedDocument(@RequestParam("sign_flag") boolean signFlag) {
        return ResponseEntity.ok(service.getSignDocument(signFlag));
    }

}
