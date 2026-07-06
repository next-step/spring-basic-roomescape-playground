package roomescape;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import roomescape.auth.AdminOnly;

@Controller
public class PageController {
    @GetMapping("/admin")
    @AdminOnly
    public String admin() {
        return "admin/index";
    }

    @GetMapping("/admin/reservation")
    @AdminOnly
    public String adminReservation() {
        return "admin/reservation";
    }

    @GetMapping("/admin/theme")
    @AdminOnly
    public String adminTheme() {
        return "admin/theme";
    }

    @GetMapping("/admin/time")
    @AdminOnly
    public String adminTime() {
        return "admin/time";
    }

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
