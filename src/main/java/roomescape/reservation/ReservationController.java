package roomescape.reservation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.CookieTokenExtractor;
import roomescape.JwtTokenProvider;
import roomescape.member.AuthService;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
public class ReservationController {

    private final Logger log =  LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService;
    private final AuthService authService;

    public ReservationController(ReservationService reservationService, AuthService authService) {
        this.reservationService = reservationService;
        this.authService = authService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll().stream().map(ReservationResponse::from).toList();
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> create(
            @Valid @RequestBody ReservationRequest reservationRequest,
            HttpServletRequest httpServletRequest
    ) {
        String username = reservationRequest.name();
        if (username == null || username.isBlank()) {
            String token = CookieTokenExtractor.extract(httpServletRequest);
            username = authService.getUsername(token);
            log.info("username: {}", username);
        }

        Reservation newReservation = reservationService.save(
                username,
                reservationRequest.date(),
                reservationRequest.theme(),
                reservationRequest.time()
        );

        return ResponseEntity.created(URI.create("/reservations/" + newReservation.getId())).body(ReservationResponse.from(newReservation));
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
