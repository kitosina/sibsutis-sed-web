package sibsutis.sed.sedsibsutis.repostiory;

import org.springframework.data.jpa.repository.JpaRepository;
import sibsutis.sed.sedsibsutis.model.entity.UserSecret;

public interface UserSecretRepository extends JpaRepository<UserSecret, String> {
}
