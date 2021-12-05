package sibsutis.sed.sedsibsutis.repository.document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sibsutis.sed.sedsibsutis.model.entity.document.SendDocumentEntity;

import java.util.List;

@Repository
public interface SendDocumentRepository extends JpaRepository<SendDocumentEntity, Long> {

    List<SendDocumentEntity> findAllByEmailSender(String emailSender);

    List<SendDocumentEntity> findAllByEmailReceiver(String emailReceiver);

    SendDocumentEntity findAllByEmailSenderAndDocumentName(String emailSender, String documentName);
}
