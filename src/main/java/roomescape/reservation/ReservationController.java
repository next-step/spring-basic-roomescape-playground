package roomescape.reservation;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import roomescape.auth.LoginMember;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.MemberService;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;

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
    public ResponseEntity create(@RequestBody @Valid ReservationRequest reservationRequest,
            @LoginMember Member loginMember) {
        Member member = resolveReservationMember(reservationRequest, loginMember);
        ReservationResponse reservation = reservationService.save(reservationRequest, member);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.id()))
                .body(reservation);
    }


    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Member resolveReservationMember(ReservationRequest reservationRequest,
            Member loginMember) {
        Member member;

        if (loginMember.getRole() == Role.ADMIN && reservationRequest.name() != null) {
            member = memberService.findByName(reservationRequest.name()).orElseThrow();
            return member;
        }
        return loginMember;
    }
}
