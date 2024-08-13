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


import java.util.List;
import java.util.Map;
import roomescape.auth.UserResponse;
import roomescape.waiting.WaitingResponse;

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

    @GetMapping("/reservations-mine")
    public List<MyReservationResponse> listMine(HttpServletRequest request) {
        String token = extractTokenFromCookies(request.getCookies());

        if (token == null) {
            return List.of();
        }

        UserResponse userResponse = authService.checkUserByToken(token);
        if (userResponse == null) {
            return List.of();
        }

        return reservationService.findMyReservations(userResponse.getName());
    }

    private String extractTokenFromCookies(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> createWaiting(@RequestBody Map<String, String> waitingRequest, HttpServletRequest request) {
        String token = extractTokenFromCookies(request.getCookies());

        if (token == null) {
            return ResponseEntity.status(401).build();
        }

        UserResponse userResponse = authService.checkUserByToken(token);
        if (userResponse == null) {
            return ResponseEntity.status(401).build();
        }

        WaitingResponse waitingResponse = reservationService.createWaiting(
                waitingRequest.get("date"),
                waitingRequest.get("time"),
                waitingRequest.get("theme"),
                userResponse.getId()
        );

        return ResponseEntity.status(201).body(waitingResponse);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> makeReservation(@RequestBody ReservationRequest reservationRequest, HttpServletRequest request) {
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

        UserResponse userResponse = authService.checkUserByToken(token);

        ReservationResponse reservationResponse = new ReservationResponse(
                1L,
                reservationRequest.getName() != null ? reservationRequest.getName() : userResponse.getName(),
                reservationRequest.getTheme(),
                reservationRequest.getDate(),
                reservationRequest.getTime()
        );

        return ResponseEntity.status(201).body(reservationResponse);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
