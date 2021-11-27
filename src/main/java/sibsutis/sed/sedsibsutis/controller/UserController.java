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

import java.security.GeneralSecurityException;

import static sibsutis.sed.sedsibsutis.controller.UserController.USER_URL;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = USER_URL)
public class UserController {

    public static final String USER_URL = "/user";

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity registerUser(@Valid @RequestBody final NewUser newUser) throws GeneralSecurityException {
        userService.register(newUser);
        return null;
    }
}
