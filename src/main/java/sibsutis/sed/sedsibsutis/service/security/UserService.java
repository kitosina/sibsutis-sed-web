package sibsutis.sed.sedsibsutis.service.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sibsutis.sed.sedsibsutis.model.dto.security.NewUser;
import sibsutis.sed.sedsibsutis.model.entity.UserSecret;
import sibsutis.sed.sedsibsutis.model.entity.security.Role;
import sibsutis.sed.sedsibsutis.model.entity.security.UserSystem;
import sibsutis.sed.sedsibsutis.repostiory.UserSecretRepository;
import sibsutis.sed.sedsibsutis.repostiory.security.UserRepository;
import sibsutis.sed.sedsibsutis.service.crypto.Crypto;
import sibsutis.sed.sedsibsutis.service.crypto.GOSTCrypto;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.Security;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Сервис для авторизации и регистрации пользователй
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserSecretRepository userSecretRepository;

    @Transactional(rollbackFor = Exception.class)
    public void register(final NewUser newUser) throws GeneralSecurityException {
        Security.addProvider(new BouncyCastleProvider());
        Crypto crypto = new GOSTCrypto();
        KeyPair keyPair = crypto.generateKeyPair();
        UserSecret userSecret = new UserSecret();
        userSecret.setKeyPrivate(keyPair.getPrivate().getEncoded());
        userSecret.setKeyPublic(keyPair.getPublic().getEncoded());
        userSecret.setEmail("nikita");
        userSecretRepository.save(userSecret);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserSystem userSystem = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден в системе"));
        return new User(userSystem.getEmail(), userSystem.getPassword(), listAuthority(userSystem.getRoles()));
    }

    /**
     * This method create list authority
     * @param roles
     * @see GrantedAuthority
     * @see Collection
     * @return Users roles
     */
    private Collection<? extends GrantedAuthority> listAuthority(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());
    }
}
