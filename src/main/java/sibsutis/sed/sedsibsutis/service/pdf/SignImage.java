package sibsutis.sed.sedsibsutis.service.pdf;


import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import sibsutis.sed.sedsibsutis.model.entity.ContragentEntity;
import sibsutis.sed.sedsibsutis.repository.ContragentRepository;
import sibsutis.sed.sedsibsutis.repository.UserSecretRepository;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;

@Service
public class SignImage {

    @Autowired
    private ContragentRepository contragentRepository;

    @Autowired
    private UserSecretRepository userSecretRepository;

    @Value("classpath:font/OpenSans-Regular.ttf")
    private Resource resource;

    @SneakyThrows
    public byte[] createSignTextImg(byte[] document, String email) {

        ContragentEntity contragentInfo = contragentRepository.findByUserSecret(
                userSecretRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("Не найден котрагент для подписи")));

        PDDocument doc = PDDocument.load(document);
        PDPage signPage = new PDPage();
        PDPageContentStream contentStream = new PDPageContentStream(doc, signPage);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Мои шрифты
        PDFont font = PDType0Font.load(PDDocument.load(document), resource.getFile());


        int cursorX = 70;
        int cursorY = 620;

        //draw rectangle
        contentStream.setNonStrokingColor(255,255,255); //white
        contentStream.fillRect(cursorX, cursorY, 140, 50);

        contentStream.setNonStrokingColor(0,191,255);//blue
        contentStream.fillRect(cursorX, cursorY, 3, 125);
        contentStream.fillRect(cursorX, cursorY, 450, 3);
        contentStream.fillRect(cursorX + 450 , cursorY, 3, 125);
        contentStream.fillRect(cursorX , cursorY + 125 , 453, 3);
        //draw text
        contentStream.setNonStrokingColor(0,191,255); //blue
        contentStream.beginText();
        //text
        contentStream.setFont(font, 12);
        contentStream.moveTextPositionByAmount(70 + 10, 700 + 25);

        contentStream.setLeading(12 * 1.2f);
        contentStream.drawString("Адрес: " + contragentInfo.getAddress());
        contentStream.newLine();
        contentStream.drawString("ИНН: "+contragentInfo.getInn());
        contentStream.newLine();
        contentStream.drawString("Университет: " + contragentInfo.getUniversity());
        contentStream.newLine();
        contentStream.drawString("Факультет: " + contragentInfo.getFaculty());
        contentStream.newLine();
        contentStream.drawString("ФИО: " + contragentInfo.getFio());
        contentStream.newLine();
        contentStream.drawString("Дата подписи: " + LocalDateTime.now());
        contentStream.newLine();
        contentStream.drawString("Статус: Подписана");

        contentStream.endText();

        //Closing the content stream
        contentStream.close();
        doc.addPage(signPage);
        doc.save(baos);
        doc.close();
        return baos.toByteArray();
    }

    @SneakyThrows
    public byte[] createNoSignTextImg(byte[] document, String email) {

        ContragentEntity contragentInfo = contragentRepository.findByUserSecret(
                userSecretRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("Не найден котрагент для подписи")));

        PDDocument doc = PDDocument.load(document);
        PDPage signPage = new PDPage();
        PDPageContentStream contentStream = new PDPageContentStream(doc, signPage);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PDFont font = PDType0Font.load(PDDocument.load(document), resource.getFile());

        int cursorX = 70;
        int cursorY = 620;

//draw rectangle
        contentStream.setNonStrokingColor(255,255,255); //white
        contentStream.fillRect(cursorX, cursorY, 100, 50);

        contentStream.setNonStrokingColor(255, 0, 0);//red
        contentStream.fillRect(cursorX, cursorY, 3, 125);
        contentStream.fillRect(cursorX, cursorY, 450, 3);
        contentStream.fillRect(cursorX + 450 , cursorY, 3, 125);
        contentStream.fillRect(cursorX , cursorY + 125 , 453, 3);
//draw text
        contentStream.setNonStrokingColor(255, 0, 0); //red
        contentStream.beginText();
//        //text
        contentStream.setFont(font, 12);
        contentStream.moveTextPositionByAmount(70 + 10, 700 + 25);

        contentStream.setLeading(12 * 1.2f);

        contentStream.drawString("Адрес: " + contragentInfo.getAddress());
        contentStream.newLine();
        contentStream.drawString("ИНН: "+contragentInfo.getInn());
        contentStream.newLine();
        contentStream.drawString("Университет: " + contragentInfo.getUniversity());
        contentStream.newLine();
        contentStream.drawString("Факультет: " + contragentInfo.getFaculty());
        contentStream.newLine();
        contentStream.drawString("ФИО: " + contragentInfo.getFio());
        contentStream.newLine();
        contentStream.drawString("Дата подписи: " + LocalDateTime.now());
        contentStream.newLine();
        contentStream.drawString("Статус: Отказано");

        contentStream.endText();

        //Closing the content stream
        contentStream.close();
        doc.addPage(signPage);
        doc.save(baos);
        doc.close();
        return baos.toByteArray();
    }

}