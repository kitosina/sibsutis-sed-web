package sibsutis.sed.sedsibsutis.service.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sibsutis.sed.sedsibsutis.model.dto.security.NewUser;
import sibsutis.sed.sedsibsutis.model.dto.user.Contragent;
import sibsutis.sed.sedsibsutis.model.entity.ContragentEntity;
import sibsutis.sed.sedsibsutis.model.entity.UserSecretEntity;
import sibsutis.sed.sedsibsutis.model.entity.security.Role;
import sibsutis.sed.sedsibsutis.model.entity.security.UserSystemEntity;
import sibsutis.sed.sedsibsutis.repository.ContragentRepository;
import sibsutis.sed.sedsibsutis.repository.UserSecretRepository;
import sibsutis.sed.sedsibsutis.repository.security.UserRepository;
import sibsutis.sed.sedsibsutis.service.crypto.RSACrypto;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для авторизации и регистрации пользователей
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ContragentRepository contragentRepository;
    private final UserInfoService userInfoService;

    /**
     * Метод для регистрации пользователя в систем
     * @param newUser данные о новогм пользователе
     * @throws GeneralSecurityException
     */
    @Transactional(rollbackFor = Exception.class)
    public void register(final NewUser newUser) throws GeneralSecurityException {
        // Сохраняем нового пользователя
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new RuntimeException(String.format("Пользователь с email: %s уже существует", newUser.getEmail()));
        } else {
            userRepository.save(convertUserNewToUserSystem(newUser));
            log.info("Пользователь с email: {} успешно зарегистрирован", newUser.getEmail());
        }

        UserSecretEntity userSecretEntity = convertUserNewToUserSecretEntity(newUser);
        ContragentEntity contragentEntity = covertUserNewToContragentEntity(newUser);
        contragentEntity.setUserSecret(userSecretEntity);
        contragentRepository.save(contragentEntity);
        // Сохраняем приватный и публичный ключ для пользователя
//        userSecretRepository.save(convertUserNewToUserSecret(newUser));
    }

    /**
     * Метод для авторизации пользователя по email (spring security)
     * @param email пользователя
     * @return UserDetails - данные пользователя
     * @throws UsernameNotFoundException
     * @see UserDetails
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserSystemEntity userSystemEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден в системе"));
        return new User(userSystemEntity.getEmail(), userSystemEntity.getPassword(), listAuthority(userSystemEntity.getRoles()));
    }

    public List<String> getAllUserReceiver() {
        return userRepository.findAll()
                .stream()
                .map(userSystemEntity -> userSystemEntity.getEmail())
                .filter(user -> !user.equals(userInfoService.getEmailAuthUser()))
                .collect(Collectors.toList());
    }

    public List<Contragent> getAllContragents() {
        return (convertContragentEntityToContragent(contragentRepository.findAll()));
    }

    /**
     * Метод для пробега по всем ролям пользователя
     * @param roles коллекция ролей пользователя
     * @see GrantedAuthority
     * @see Collection
     * @return коллекция ролей пользователя
     */
    private Collection<? extends GrantedAuthority> listAuthority(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());
    }

    private UserSystemEntity convertUserNewToUserSystem(final NewUser newUser) {
        final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return new UserSystemEntity().setEmail(newUser.getEmail())
                .setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()))
                .setRoles(Collections.singleton(Role.USER));
    }

    private UserSecretEntity convertUserNewToUserSecretEntity(final NewUser newUser) throws GeneralSecurityException {
        final RSACrypto crypto = new RSACrypto();
        final KeyPair keyPair = crypto.generateKeyPair();
        return new UserSecretEntity().setEmail(newUser.getEmail())
                .setKeyPublic(keyPair.getPublic().getEncoded())
                .setKeyPrivate(keyPair.getPrivate().getEncoded());
    }
    
    private ContragentEntity covertUserNewToContragentEntity(final NewUser newUser) {
        return new ContragentEntity()
                .setAddress(newUser.getContragentInfo().getAddress())
                .setFaculty(newUser.getContragentInfo().getFaculty())
                .setFio(newUser.getContragentInfo().getFio())
                .setInn(newUser.getContragentInfo().getInn())
                .setUniversity(newUser.getContragentInfo().getUniversity());
    }

    private List<Contragent> convertContragentEntityToContragent(final List<ContragentEntity> contragentEntities) {
        return contragentEntities.stream()
                .map(contragentEntity ->
                    Contragent.builder()
                            .address(contragentEntity.getAddress())
                            .email(contragentEntity.getUserSecret().getEmail())
                            .faculty(contragentEntity.getFaculty())
                            .fio(contragentEntity.getFio())
                            .university(contragentEntity.getUniversity())
                            .inn(contragentEntity.getInn())
                            .build()
                ).collect(Collectors.toList());
    }
}
