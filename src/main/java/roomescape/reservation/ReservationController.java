package roomescape.reservation;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import roomescape.member.LoginMember;

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
    public ResponseEntity<ReservationResponse> create(
        @RequestBody ReservationRequest reservationRequest, LoginMember member
    ) {
        if (reservationRequest.getDate() == null
            || reservationRequest.getTheme() == null
            || reservationRequest.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (reservationRequest.getName() == null) {
            reservationRequest = new ReservationRequest(
                member.getName(), reservationRequest.getDate(), reservationRequest.getTheme(), reservationRequest.getTime()
            );
        }

        ReservationResponse reservation = reservationService.save(reservationRequest);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
