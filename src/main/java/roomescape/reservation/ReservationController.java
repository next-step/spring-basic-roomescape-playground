package roomescape.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.member.LoginMember;
import roomescape.member.MemberRepository;
import roomescape.waiting.WaitingService;
import roomescape.waiting.WaitingResponse;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ReservationController {
    private final ReservationService reservationService;
    private final WaitingService waitingService;
    private final MemberRepository memberRepository;

    public ReservationController(MemberRepository memberRepository, ReservationService reservationService, WaitingService waitingService) {
        this.memberRepository = memberRepository;
        this.reservationService = reservationService;
        this.waitingService = waitingService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @PostMapping("/reservations")
    public ResponseEntity create(@RequestBody ReservationRequest reservationRequest, LoginMember loginMember) {

        if (loginMember != null && reservationRequest.getName() == null) { // 멤버가 아닌 경우 (참고)
            reservationRequest.setName(loginMember.getName());
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

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations-mine")
    public List<MyReservationResponse> mineReservation(LoginMember loginMember) {
        List<MyReservationResponse> reservations = reservationService.findReservation(loginMember);
        List<MyReservationResponse> waitings = waitingService.findWaitingsByMember(loginMember);

        reservations.addAll(waitings);
        return reservations;
    }

}