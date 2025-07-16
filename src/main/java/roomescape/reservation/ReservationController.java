package roomescape.reservation;

import auth.annotation.Login;
import auth.dto.LoginMember;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> list() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> create(@RequestBody ReservationRequest request, @Login LoginMember loginMember) {
        ReservationResponse reservation = reservationService.create(request, loginMember);
        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @GetMapping("/reservations/{id}")
    public ResponseEntity<ReservationResponse> findReservationById(@PathVariable Long id) {
        ReservationResponse reservation = reservationService.findResponseById(id);
        return ResponseEntity.ok(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations-mine")
    public ResponseEntity<List<MyReservationResponse>> findMyReservations(@Login LoginMember loginMember) {
        List<MyReservationResponse> myReservations = reservationService.findMyReservations(loginMember.getId());
        return ResponseEntity.ok(myReservations);
    }
}
