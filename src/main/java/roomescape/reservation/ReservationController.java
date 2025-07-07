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
import roomescape.auth.AuthenticatedMember;
import roomescape.auth.LoginMember;

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
            @RequestBody ReservationRequest req,
            @AuthenticatedMember LoginMember loginMember) {

        ReservationResponse response;
        if (req.getName() == null || req.getName().isBlank()) {
            response = reservationService.saveUser(req, loginMember);
        }
        else {
            response = reservationService.saveAdmin(req);
        }

        return ResponseEntity
                .created(URI.create("/reservations/" + response.getId()))
                .body(response);
    }


    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations-mine")
    public ResponseEntity<List<MyReservationResponse>> listMyReservation(
            @AuthenticatedMember LoginMember loginMember) {

        List<MyReservationResponse> mine = reservationService.findMine(loginMember);
        return ResponseEntity.ok(mine);
    }
}
