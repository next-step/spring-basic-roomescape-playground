package roomescape.reservation;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.Login.LoginMember;
import roomescape.jwt.JwtController;
import roomescape.jwt.JwtTokenMember;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final JwtController jwtController;

    public ReservationController(ReservationService reservationService,JwtController jwtController) {
        this.reservationService = reservationService;
        this.jwtController = jwtController;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @PostMapping("/reservations")
    public ResponseEntity create(@RequestBody ReservationRequest reservationRequest, @CookieValue("token") String token, LoginMember member) {
        if (reservationRequest.date() == null
                || reservationRequest.theme() == null
                || reservationRequest.time() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (reservationRequest.name() == null) {
            reservationRequest = new ReservationRequest(
                    member.name(),
                    reservationRequest.date(),
                    reservationRequest.theme(),
                    reservationRequest.time()
            );
        }

        ReservationResponse reservation = reservationService.save(reservationRequest);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
