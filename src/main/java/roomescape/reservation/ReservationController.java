package roomescape.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import roomescape.auth.AuthMember;
import roomescape.member.Member;
import roomescape.member.Role;

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

    @GetMapping("/reservations-mine")
    public ResponseEntity<List<MemberReservationResponse>> mine(@AuthMember Member member) {
        List<MemberReservationResponse> result = reservationService.getMyReservations(member.getId());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/reservations")
    public ResponseEntity create(@AuthMember Member member
            , @RequestBody ReservationRequest reservationRequest) {
        Role role = member.getRole();
        boolean isAdmin = role.isAdmin();
        String name = reservationRequest.name();
        if (name == null || name.isEmpty() || !isAdmin) {
            reservationRequest = reservationRequest.update(member.getName());
        }

        ReservationResponse result = getReservationResponse(member,
                reservationRequest, isAdmin);

        return ResponseEntity.created(URI.create("/reservations/" + result.id()))
                .body(result);
    }

    private ReservationResponse getReservationResponse(Member member,
                                                       ReservationRequest reservationRequest,
                                                       boolean isAdmin) {
        if (isAdmin) {
            return reservationService.save(reservationRequest);
        }
        return reservationService.saveWithMember(reservationRequest, member);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
