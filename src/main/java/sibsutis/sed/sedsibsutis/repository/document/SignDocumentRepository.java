package sibsutis.sed.sedsibsutis.repository.document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sibsutis.sed.sedsibsutis.model.entity.document.SignDocumentEntity;

import java.util.List;

@Repository
public interface SignDocumentRepository extends JpaRepository<SignDocumentEntity, Long> {

    List<SignDocumentEntity> findAllByEmailReceiverAndSignFlag(String emailReceiver, boolean signFlag);

}
