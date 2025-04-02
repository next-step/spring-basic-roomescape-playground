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
import roomescape.waiting.WaitingRankingResponse;
import roomescape.waiting.WaitingService;

@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final WaitingService waitingService;

    public ReservationController(ReservationService reservationService,
                                 WaitingService waitingService) {
        this.reservationService = reservationService;
        this.waitingService = waitingService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @GetMapping("/reservations-mine")
    public ResponseEntity<MemberReservationResponses> getMemberReservations(
            @AuthMember Member member) {
        MemberReservationResponses results = new MemberReservationResponses(
                reservationService.getMemberReservations(member.getId()));
        List<WaitingRankingResponse> memberWaitings = waitingService.getMemberWaitings(member);

        return ResponseEntity.ok(results.addWaitings(memberWaitings));
    }

    // fixme: 중복 예약 허용 x
    @PostMapping("/reservations")
    public ResponseEntity create(@AuthMember Member member
            , @RequestBody ReservationRequest reservationRequest) {
        Role role = member.getRole();
        boolean isAdmin = role.isAdmin();
        String name = reservationRequest.name();
        if (name == null || name.isEmpty() || !isAdmin) {
            reservationRequest = reservationRequest.update(member.getName());
        }

        ReservationResponse result = create(member, reservationRequest, isAdmin);

        return ResponseEntity.created(URI.create("/reservations/" + result.id()))
                .body(result);
    }

    private ReservationResponse create(Member member, ReservationRequest reservationRequest,
                                       boolean isAdmin) {
        if (isAdmin) {
            return reservationService.create(reservationRequest);
        }
        return reservationService.saveWithMember(reservationRequest, member);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
