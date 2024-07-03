package roomescape.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.login.LoginMember;
import roomescape.member.MemberService;
import roomescape.member.ViewMemberResponse;

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

    @PostMapping("/reservations")
    public ResponseEntity create(@RequestBody ReservationRequest reservationRequest, LoginMember loginMember) {
        if (reservationRequest.getDate() == null || reservationRequest.getTheme() == null || reservationRequest.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }

        String memberName = reservationRequest.getName() != null ? reservationRequest.getName() : loginMember.getName();
        ViewMemberResponse member = memberService.findMemberByName(memberName);
        if (member == null) {
            return ResponseEntity.badRequest().build();
        }

        reservationRequest.setName(member.getName()); // 예약자의 이름을 설정
        ReservationResponse reservation = reservationService.save(reservationRequest);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
