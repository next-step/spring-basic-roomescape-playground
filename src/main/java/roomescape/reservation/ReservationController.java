package roomescape.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberService;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final MemberService memberService;

    public ReservationController(ReservationService reservationService,
                                 MemberService memberService) {
        this.reservationService = reservationService;
        this.memberService = memberService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @PostMapping("/reservations")
    public ResponseEntity create(@RequestBody ReservationRequest reservationRequest, LoginMember loginMember) {
        if (reservationRequest.date() == null
                || reservationRequest.theme() == null
                || reservationRequest.time() == null) {
            return ResponseEntity.badRequest().build();
        }

        ReservationResponse reservation;

        if (loginMember != null) {
            Member member = memberService.findById(loginMember.id());
            reservation = reservationService.saveMember(reservationRequest, member);
        } else {
            if (reservationRequest.name() == null || reservationRequest.name().isBlank()) {
                return ResponseEntity.badRequest().build();
            }
            reservation = reservationService.saveAdmin(reservationRequest);
        }

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations-mine")
    public ResponseEntity<List<MyReservationResponse>> mine(LoginMember loginMember) {
        if (loginMember == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(reservationService.findMine(loginMember.id())
        );
    }

}
