package roomescape.page.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerPageController {
    @GetMapping("/")
    public String reservation() {
        return "reservation";
    }

    @GetMapping("/reservation-mine")
    public String myReservation() {
        return "reservation-mine";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
}
