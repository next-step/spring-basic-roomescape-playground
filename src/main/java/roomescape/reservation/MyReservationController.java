package roomescape.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import roomescape.member.LoginMember;
import roomescape.waiting.WaitingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MyReservationController {

    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;

    public MyReservationController(ReservationRepository reservationRepository, WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.waitingRepository = waitingRepository;
    }

    @GetMapping("/reservation-mine")
    public String myReservationPage() {
        return "reservation-mine";
    }

    @GetMapping("/reservations-mine")
    @ResponseBody
    public ResponseEntity<List<MyReservationResponse>> getMyReservations(LoginMember loginMember) {
        List<MyReservationResponse> reservations = reservationRepository.findByMemberId(loginMember.getId()).stream()
                .map(MyReservationResponse::from)
                .collect(Collectors.toList());

        List<MyReservationResponse> waitings = waitingRepository.findWaitingsWithRankByMemberId(loginMember.getId()).stream()
                .map(MyReservationResponse::from)
                .collect(Collectors.toList());

        List<MyReservationResponse> result = new ArrayList<>();
        result.addAll(reservations);
        result.addAll(waitings);

        return ResponseEntity.ok(result);
    }
}
