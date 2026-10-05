package roomescape.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.login.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberService;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final MemberService memberService;

    public ReservationController(ReservationService reservationService, MemberService memberService) {
        this.reservationService = reservationService;
        this.memberService = memberService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @GetMapping("/reservations-mine")
    public List<MyReservationResponse> findMyReservations(LoginMember loginMember) {
        return reservationService.findMyReservations(loginMember.getId());
    }

    @PostMapping("/reservations")
    public ResponseEntity create(@RequestBody ReservationRequest reservationRequest, LoginMember loginMember) {
        if (reservationRequest.getDate() == null
                || reservationRequest.getTheme() == null
                || reservationRequest.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }

        Member member = null;

        // 요청으로 들어온 게 null이면 사용자니까
        if (!loginMember.getRole().equals("ADMIN")) {
            member = memberService.findById(loginMember.getId());
        }

        ReservationResponse reservation = reservationService.save(reservationRequest, member);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, LoginMember loginMember) {
        reservationService.deleteById(id, loginMember.getId());
        return ResponseEntity.noContent().build();
    }
}
