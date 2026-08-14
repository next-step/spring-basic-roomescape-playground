package roomescape.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;

import roomescape.waiting.Waiting;
import roomescape.waiting.WaitingRepository;
import roomescape.waiting.WaitingWithRank;

import java.util.ArrayList;
import java.util.List;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations") //예약 목록 반환
    public List<Reservation> list() {
        return reservationService.findAll();
    }

    @PostMapping("/reservations")
    public ResponseEntity create(
            @RequestBody ReservationRequest reservationRequest,
            Member loginMember //현재 로그인한 회원
    ) {
        Reservation saved = reservationService.createReservation(reservationRequest, loginMember);

        return ResponseEntity.created(URI.create("/reservations/" + saved.getId())).body(saved);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    //예약 조회할 때 대기 목록까지 같이 반환하도록 변경
    @GetMapping("/reservations-mine")
    public List<MyReservationResponse> listMine(Member loginMember) {
        return reservationService.findMyReservations(loginMember);
    }

}

