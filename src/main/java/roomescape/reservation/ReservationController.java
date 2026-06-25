package roomescape.reservation;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.loginmember.LoginMember;
import roomescape.member.Member;

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

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> create(@RequestBody ReservationRequest reservationRequest,
                                                      @LoginMember Member member) {
        if (reservationRequest.getDate() == null
                || reservationRequest.getTheme() == null
                || reservationRequest.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }

        ReservationRequest request;
        if (reservationRequest.getName() == null) {
            request = new ReservationRequest(
                    member.getName(),
                    reservationRequest.getDate(),
                    reservationRequest.getTheme(),
                    reservationRequest.getTime()
            );
        } else if (Objects.equals(member.getRole(), "Admin")) {
            request = reservationRequest;
        } else {
            throw new IllegalArgumentException("관리자 이외에는 자신의 이름으로만 예약할 수 있습니다");
        }

        ReservationResponse reservation = reservationService.save(request);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
