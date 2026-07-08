package roomescape.reservation;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.loginmember.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRole;
import roomescape.waiting.WaitingService;

@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final WaitingService waitingService;

    public ReservationController(ReservationService reservationService, WaitingService waitingService) {
        this.reservationService = reservationService;
        this.waitingService = waitingService;
    }

    private static ReservationRequest checkRequestName(ReservationRequest reservationRequest, Member member) {
        ReservationRequest request;
        if (reservationRequest.name() == null) {
            request = new ReservationRequest(
                    member.getName(),
                    reservationRequest.date(),
                    reservationRequest.theme(),
                    reservationRequest.time()
            );
        } else if (Objects.equals(member.getRole(), MemberRole.ADMIN.toString())) {
            request = reservationRequest;
        } else {
            throw new IllegalArgumentException("관리자 이외에는 자신의 이름으로만 예약할 수 있습니다");
        }
        return request;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> create(@RequestBody ReservationRequest reservationRequest,
                                                      @LoginMember Member member) {
        if (reservationRequest.date() == null
                || reservationRequest.theme() == null
                || reservationRequest.time() == null) {
            return ResponseEntity.badRequest().build();
        }

        ReservationRequest request = checkRequestName(reservationRequest, member);

        ReservationResponse reservation = reservationService.save(request);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.id())).body(reservation);
    }

    @GetMapping("/reservations-mine")
    public List<MyReservationResponse> getMyReservations(@LoginMember Member member) {
        return Stream.concat(reservationService.findMyReservations(member).stream(),
                waitingService.findMyWaitings(member).stream()).collect(Collectors.toList());
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
