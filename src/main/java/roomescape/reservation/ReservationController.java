package roomescape.reservation;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.LoginMember;
import roomescape.member.MemberService;
import roomescape.waiting.WaitingRequest;
import roomescape.waiting.WaitingResponse;
import roomescape.waiting.WaitingService;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final MemberService memberService;
    private final ReservationRepository reservationRepository;

    private final WaitingService waitingService;

    public ReservationController(ReservationService reservationService, MemberService memberService, ReservationRepository reservationRepository,  WaitingService waitingService) {
        this.reservationService = reservationService;
        this.memberService=memberService;
        this.reservationRepository = reservationRepository;
        this.waitingService =waitingService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @PostMapping("/reservations")
    public ResponseEntity create(@RequestBody ReservationRequest reservationRequest, LoginMember member) {

        if (reservationRequest.getDate() == null
                || reservationRequest.getTheme() == null
                || reservationRequest.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }
        ReservationResponse reservation = reservationService.save(reservationRequest,member.getId());

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations-mine")
    public List<MyReservationResponse> listMine(HttpServletRequest request) {
           List<MyReservationResponse> myReservationResponseList = reservationService.findMyReservation(request);
           return myReservationResponseList;
    }

    @PostMapping("/waitings")
    public ResponseEntity createWaiting(@RequestBody WaitingRequest waitingRequest, HttpServletRequest request){
        WaitingResponse waitingResponse =waitingService.save(waitingRequest, request);

        return ResponseEntity.created(URI.create("/waitings/" +waitingResponse.getId() )).body(waitingResponse);
    }
}
