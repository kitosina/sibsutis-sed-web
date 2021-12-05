package sibsutis.sed.sedsibsutis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sibsutis.sed.sedsibsutis.model.entity.UserSecretEntity;

import java.util.Optional;

@Repository
public interface UserSecretRepository extends JpaRepository<UserSecretEntity, String> {
    Optional<UserSecretEntity> findByEmail(String email);
}
