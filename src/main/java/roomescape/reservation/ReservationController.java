package roomescape.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.Login.LoginMember;
import roomescape.jwt.JwtUtils;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final JwtUtils jwtUtils;

    public ReservationController(ReservationService reservationService, JwtUtils jwtUtils) {
        this.reservationService = reservationService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @GetMapping("/reservations-mine")
    public List<MyReservationResponse> findReservations(LoginMember loginMember) {
        return reservationService.findMyReservations(loginMember);
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

        return ResponseEntity.created(URI.create("/reservations/" + reservation.id())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
