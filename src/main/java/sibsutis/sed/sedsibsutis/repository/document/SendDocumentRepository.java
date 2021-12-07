package sibsutis.sed.sedsibsutis.repository.document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sibsutis.sed.sedsibsutis.model.entity.document.SendDocumentEntity;

import java.util.List;

@Repository
public interface SendDocumentRepository extends JpaRepository<SendDocumentEntity, Long> {

    List<SendDocumentEntity> findAllByEmailSender(String emailSender);

    List<SendDocumentEntity> findAllByEmailReceiver(String emailReceiver);

    SendDocumentEntity findAllByEmailSenderAndDocumentName(String emailSender, String documentName);

    @Modifying
    @Query(nativeQuery = true, value = "update document.send_document set email_receiver = null\n" +
            "        where email_sender = ?1 and document_name = ?2 and email_receiver = ?3")
    void updateReceiverSign(String emailSender, String documentName, String emailReceiver);
}
