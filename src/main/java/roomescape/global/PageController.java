package roomescape.global;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import roomescape.global.login.Role;
import roomescape.global.login.RoleCheck;

@Controller
public class PageController {
    @GetMapping("/admin")
    @RoleCheck(role = Role.ADMIN)
    public String admin() {
        return "admin/index";
    }

    @GetMapping("/admin/reservation")
    @RoleCheck(role = Role.ADMIN)
    public String adminReservation() {
        return "admin/reservation";
    }

    @GetMapping("/admin/theme")
    @RoleCheck(role = Role.ADMIN)
    public String adminTheme() {
        return "admin/theme";
    }

    @GetMapping("/admin/time")
    @RoleCheck(role = Role.ADMIN)
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
