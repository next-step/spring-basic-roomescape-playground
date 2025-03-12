package roomescape.reservation;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.LoginMember;

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
                                                      LoginMember loginMember) {
        /**
         * 예약 생성 시 ReservationReqeust의 name이 없는 경우 Cookie에 담긴 정보를 활용하도록 리팩터링 합니다.
         * ReservationReqeust에 name값이 있으면 name으로 Member를 찾고
         * 없으면 로그인 정보를 활용해서 Member를 찾도록 수정합니다.
         */
        ReservationResponse reservation = Optional.ofNullable(reservationRequest.getName())
                .map(name -> reservationService.save(reservationRequest))
                .orElseGet(() -> reservationService.save(reservationRequest, loginMember));

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
