package sibsutis.sed.sedsibsutis.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static sibsutis.sed.sedsibsutis.controller.HealthCheckController.HEALTH_URL;

/**
 * Точка входа для проверки службы
 */
@RestController
@RequestMapping(HEALTH_URL)
public class HealthCheckController {

    public static final String HEALTH_URL = "/check";

    private static final String HEALTH_CHECK_URL = "/register";

    /**
     * Метод для проверки службы
     *
     * @return строку ок
     */
    @RequestMapping(HEALTH_CHECK_URL)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    String check() {
        return "ok";
    }

}
