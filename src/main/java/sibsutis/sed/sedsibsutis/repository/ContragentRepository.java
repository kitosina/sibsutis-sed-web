package sibsutis.sed.sedsibsutis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sibsutis.sed.sedsibsutis.model.entity.ContragentEntity;
import sibsutis.sed.sedsibsutis.model.entity.UserSecretEntity;

import java.util.List;

public interface ContragentRepository extends JpaRepository<ContragentEntity, Long> {
    ContragentEntity findByUserSecret(UserSecretEntity email);
}
