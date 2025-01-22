package roomescape.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.authentication.MemberAuthInfo;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @PostMapping("/reservations")
    public ResponseEntity create(@RequestBody ReservationRequest reservationRequest, MemberAuthInfo memberAuthInfo) {
        ReservationResponse reservation = reservationService.save(reservationRequest, memberAuthInfo);
        return ResponseEntity.created(URI.create("/reservations/" + reservation.id())).body(reservation);
    }

    @GetMapping("/reservations-mine")
    public List<MyReservationResponse> myReservationLists(MemberAuthInfo memberAuthInfo){
        List<MyReservationResponse> myReservations = reservationService.findMyReservations(memberAuthInfo);
        return myReservations;
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
