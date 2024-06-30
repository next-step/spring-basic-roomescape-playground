package roomescape.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.member.LoginMember;
import roomescape.member.MemberRepository;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ReservationController {
    private final ReservationService reservationService;
    private final MemberRepository memberRepository;

    public ReservationController(MemberRepository memberRepository, ReservationService reservationService) {
        this.memberRepository = memberRepository;
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @PostMapping("/reservations")
    public ResponseEntity create(@RequestBody ReservationRequest reservationRequest, LoginMember loginmember) {

        if(loginmember != null && reservationRequest.getName()==null) { //멤버가 아닌경우 (참고)
            reservationRequest.setName(loginmember.getName());
        }

        if (reservationRequest.getName() == null
                || reservationRequest.getDate() == null
                || reservationRequest.getTheme() == null
                || reservationRequest.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }
        ReservationResponse reservation = reservationService.save(reservationRequest);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @GetMapping("/reservations-mine")
    public List<MyReservationResponse> mineReservation(LoginMember loginMember) {
        return reservationService.findReservation(loginMember);
    }
}