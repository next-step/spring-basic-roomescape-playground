package roomescape.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.LoginMember;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @PostMapping("/reservations")
    public ResponseEntity create(@RequestBody ReservationRequest reservationReq, LoginMember member) {
        if (reservationReq.getDate() == null
                || reservationReq.getTheme() == null
                || reservationReq.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (reservationReq.getName() == null) {
            reservationReq = new ReservationRequest(
                    member.name(),
                    reservationReq.getDate(),
                    reservationReq.getTheme(),
                    reservationReq.getTime());
        }

        ReservationResponse reservation = reservationService.save(reservationReq);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
