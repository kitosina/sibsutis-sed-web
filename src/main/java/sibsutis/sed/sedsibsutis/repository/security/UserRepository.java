package sibsutis.sed.sedsibsutis.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sibsutis.sed.sedsibsutis.model.entity.security.UserSystemEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserSystemEntity, Long> {

    Optional<UserSystemEntity> findByEmail(String email);

}
