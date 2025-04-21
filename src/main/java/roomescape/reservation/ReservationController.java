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
    public List<ReservationResponse> getList() {
        return reservationService.findAll();
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createForUser(
            @RequestBody ReservationRequest reservationRequest,
            LoginMember loginMember
    ) {
        ReservationResponse reservationResponse = reservationService.saveForUser(reservationRequest, loginMember);
        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.getId()))
                .body(reservationResponse);
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> createForAdmin(
            @RequestBody ReservationRequest reservationRequest
    ) {
        ReservationResponse reservationResponse = reservationService.saveForAdmin(reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.getId()))
                .body(reservationResponse);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, LoginMember loginMember) {
        reservationService.deleteById(id, loginMember);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations-mine")
    public List<MyReservationResponse> findMyReservations(LoginMember loginMember) {
        return reservationService.findByMemberId(loginMember.getId());
    }

    @DeleteMapping("/admin/reservations/{id}")
    public ResponseEntity<Void> deleteByAdmin(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
