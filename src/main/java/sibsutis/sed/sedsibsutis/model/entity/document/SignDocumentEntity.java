package sibsutis.sed.sedsibsutis.model.entity.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "sign_document", schema = "document")
public class SignDocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "document_name")
    private String documentName;

    @Column(name = "email_sender")
    private String emailSender;

    @Column(name = "email_receiver")
    private String emailReceiver;

    @Column(name = "flag_sign")
    private Boolean signFlag;

}
