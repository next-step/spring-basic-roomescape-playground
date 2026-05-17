package roomescape;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Controller
public class PageController {
    MemberDao memberDao;

    public PageController(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @GetMapping("/admin")
    public String admin(@CookieValue(value = "token", required = false) String token, HttpServletResponse response) {
        if (token == null) {
            response.setStatus(401);
            return null;
        }

        String memberId = Jwts.parser()
                .setSigningKey("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789AB")
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        Member member = memberDao.findById(Integer.parseInt(memberId));
        if (member.getRole().equals("ADMIN"))
            return "admin/index";
        else
            response.setStatus(401);
            return null;

    }

    @GetMapping("/admin/reservation")
    public String adminReservation() {
        return "admin/reservation";
    }

    @GetMapping("/admin/theme")
    public String adminTheme() {
        return "admin/theme";
    }

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
