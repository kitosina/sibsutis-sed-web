package sibsutis.sed.sedsibsutis.controller;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sibsutis.sed.sedsibsutis.service.DocumentService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@Slf4j
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @RequestMapping(value = "/document", method = RequestMethod.POST, consumes = { "multipart/form-data" })
    public ResponseEntity getDoc(@RequestParam("uploadfile") MultipartFile pdfFile) throws IOException, GeneralSecurityException, OperatorCreationException, CMSException {
        byte[] contents  = pdfFile.getBytes();
        log.info(new String(contents));
        byte[] contest = documentService.get(contents);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Here you have to set the actual filename of your pdf
        String filename = "output.pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contest, headers, HttpStatus.OK);
        return response;
    }
}
