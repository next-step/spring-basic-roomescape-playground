package roomescape.reservation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.AuthService;
import roomescape.auth.LoginReservationResponse;
import roomescape.auth.LoginResponse;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final AuthService authService;

    public ReservationController(ReservationService reservationService, AuthService authService) {
        this.reservationService = reservationService;
        this.authService = authService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @PostMapping("/reservations")
    public ResponseEntity<LoginReservationResponse> makeReservation(@RequestBody Map<String, String> reservationRequest, HttpServletRequest request) {
        String token = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            return ResponseEntity.status(401).build();
        }

        LoginResponse loginResponse = authService.findUserByToken(token);

        LoginReservationResponse loginReservationResponse = new LoginReservationResponse(loginResponse.getEmail());

        return ResponseEntity.status(201).body(loginReservationResponse);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
