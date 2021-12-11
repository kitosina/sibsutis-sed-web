package sibsutis.sed.sedsibsutis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/home_page")
    public String home_page() {
        return "home_page";
    }

    @GetMapping("/incoming")
    public String incoming() {
        return "incoming";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/refusal")
    public String refusal() {
        return "refusal";
    }
    @GetMapping("/send")
    public String send() {
        return "send";
    }

    @GetMapping("/sent")
    public String sent() {
        return "sent";
    }

    @GetMapping("/signed")
    public String signed() {
        return "signed";
    }

    @GetMapping("/contragent")
    public String contragent() {
        return "contragent";
    }

}
