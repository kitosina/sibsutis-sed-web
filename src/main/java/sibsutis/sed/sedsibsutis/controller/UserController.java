package sibsutis.sed.sedsibsutis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sibsutis.sed.sedsibsutis.model.dto.security.NewUser;
import sibsutis.sed.sedsibsutis.service.security.UserService;

import javax.validation.Valid;


import static sibsutis.sed.sedsibsutis.controller.UserController.USER_URL;

@RequiredArgsConstructor
@RestController
@RequestMapping(USER_URL)
public class UserController {

    public static final String USER_URL = "/user";

    private static final String USER_URL_REGISTER = "/register";

    private final UserService userService;

    /**
     * Метод для заведения нового пользователя в систему (только Админ)
     * @param newUser информация о новом пользователе
     * @return Status??
     * @throws Exception
     */
//    TODO: сделать только для админа
    @PostMapping(USER_URL_REGISTER)
    public ResponseEntity registerUser(@Valid @RequestBody final NewUser newUser) throws Exception {
        userService.register(newUser);
        return null;
    }
}
