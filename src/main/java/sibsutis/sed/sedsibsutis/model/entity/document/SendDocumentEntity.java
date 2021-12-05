package sibsutis.sed.sedsibsutis.model.entity.document;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "send_document", schema = "document")
public class SendDocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email_receiver")
    private String emailReceiver;

    @Column(name = "email_sender")
    private String emailSender;

    @Column(name = "document_name")
    private String documentName;

    @Column(name = "document")
    private byte[] document;

}
