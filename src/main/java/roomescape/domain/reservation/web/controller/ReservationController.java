package roomescape.domain.reservation.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.member.service.AuthService;
import roomescape.domain.reservation.web.dto.ReservationRequest;
import roomescape.domain.reservation.web.dto.ReservationResponse;
import roomescape.domain.reservation.service.ReservationService;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.global.auth.jwt.CookieTokenExtractor;

import java.net.URI;
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
