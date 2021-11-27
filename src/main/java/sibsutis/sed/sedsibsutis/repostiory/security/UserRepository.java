package sibsutis.sed.sedsibsutis.repostiory.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sibsutis.sed.sedsibsutis.model.entity.security.UserSystem;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserSystem, Long> {

    Optional<UserSystem> findByEmail(String email);

}
