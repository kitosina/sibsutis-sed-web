package sibsutis.sed.sedsibsutis.service.pdf;


import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;

public class SignImage {

    @SneakyThrows
    public byte[] createSignTextImg(byte[] document, String email) {
        PDDocument doc = PDDocument.load(document);
        PDPage signPage = new PDPage();
        PDPageContentStream contentStream = new PDPageContentStream(doc, signPage);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PDFont font = PDType1Font.TIMES_ROMAN;

        int cursorX = 70;
        int cursorY = 730;

        //draw rectangle
        contentStream.setNonStrokingColor(255,255,255); //white
        contentStream.fillRect(cursorX, cursorY, 140, 50);

        contentStream.setNonStrokingColor(0,191,255);//blue
        contentStream.fillRect(cursorX, cursorY, 3, 50);
        contentStream.fillRect(cursorX, cursorY, 140, 3);
        contentStream.fillRect(cursorX + 140, cursorY, 3, 53);
        contentStream.fillRect(cursorX + 140, cursorY + 50, -140, 3);
        //draw text
        contentStream.setNonStrokingColor(0,191,255); //blue
        contentStream.beginText();
        //text
        contentStream.setFont(font, 12);
        contentStream.moveTextPositionByAmount(cursorX + 10, cursorY + 25);
        contentStream.drawString(email + " Signature");
        contentStream.endText();

        //Closing the content stream
        contentStream.close();
        doc.addPage(signPage);
        doc.save(baos);
        doc.close();
        return baos.toByteArray();
    }

    @SneakyThrows
    public byte[] createNoSignTextImg(byte[] document) {
        PDDocument doc = PDDocument.load(document);
        PDPage signPage = new PDPage();
        PDPageContentStream contentStream = new PDPageContentStream(doc, signPage);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PDFont font = PDType1Font.TIMES_ROMAN;

        int cursorX = 70;
        int cursorY = 730;

//draw rectangle
        contentStream.setNonStrokingColor(255,255,255); //white
        contentStream.fillRect(cursorX, cursorY, 100, 50);

        contentStream.setNonStrokingColor(255, 0, 0);//red
        contentStream.fillRect(cursorX, cursorY, 3, 50);
        contentStream.fillRect(cursorX, cursorY, 100, 3);
        contentStream.fillRect(cursorX + 100, cursorY, 3, 53);
        contentStream.fillRect(cursorX + 100, cursorY + 50, -100, 3);
//draw text
        contentStream.setNonStrokingColor(255, 0, 0); //red
        contentStream.beginText();
//        //text
        contentStream.setFont(font, 12);
        contentStream.moveTextPositionByAmount(cursorX + 10, cursorY + 20);
        contentStream.drawString("Signature refused");
        contentStream.endText();

        //Closing the content stream
        contentStream.close();
        doc.addPage(signPage);
        doc.save(baos);
        doc.close();
        return baos.toByteArray();
    }

}