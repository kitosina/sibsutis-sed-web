package sibsutis.sed.sedsibsutis.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Точка входа для проверки службы
 */
@RestController
@RequestMapping(value = "/check")
public class HealthCheckController {

    /**
     * Метод для проверки службы
     *
     * @return строку ок
     */
    @RequestMapping(value = "/healthcheck")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    String check() {
        return "ok";
    }

}
