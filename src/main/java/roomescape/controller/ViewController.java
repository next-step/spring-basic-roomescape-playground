package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import roomescape.auth.AdminRoute;

@Controller
public class ViewController {
    @AdminRoute
    @GetMapping("/admin")
    public String admin() {
        return "admin/index";
    }

    @AdminRoute
    @GetMapping("/admin/reservation")
    public String adminReservation() {
        return "admin/reservation";
    }

    @AdminRoute
    @GetMapping("/admin/theme")
    public String adminTheme() {
        return "admin/theme";
    }

    @AdminRoute
    @GetMapping("/admin/time")
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
