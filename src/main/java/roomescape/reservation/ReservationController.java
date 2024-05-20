package roomescape.reservation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.member.Member;
import roomescape.member.MemberService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final MemberService memberService;

    public ReservationController(ReservationService reservationService, MemberService memberService) {
        this.reservationService = reservationService;
        this.memberService = memberService;
    }


//    @GetMapping("/reservations")
//    public List<ReservationResponse> list() {
//        return reservationService.findAll();
//    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> list(Member member) {
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<ReservationResponse> reservations = reservationService.findAllByMember(member.getId());
        return ResponseEntity.ok(reservations);
    }

    @PostMapping("/reservations")
    public ResponseEntity create(@RequestBody ReservationRequest reservationRequest, Member member) {
        if (reservationRequest.getDate() == null || reservationRequest.getTheme() == null || reservationRequest.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (reservationRequest.getName() == null && member != null) {
            reservationRequest.setName(member.getName());
        }

        ReservationResponse reservation = reservationService.save(reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }
//@GetMapping("/reservations")
//public ResponseEntity<List<ReservationResponse>> list(Member member) {
//    if (member == null) {
//        // 로그인하지 않은 경우 처리
//        // 예를 들어, 로그인 페이지로 리다이렉트하거나 인증 오류 응답을 반환할 수 있습니다.
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//    }
//    List<ReservationResponse> reservations = reservationService.findAll();
//    return ResponseEntity.ok(reservations);
//}



//    @PostMapping("/reservations")
//    public ResponseEntity create(@RequestBody ReservationRequest reservationRequest) {
//        if (reservationRequest.getName() == null
//                || reservationRequest.getDate() == null
//                || reservationRequest.getTheme() == null
//                || reservationRequest.getTime() == null) {
//            return ResponseEntity.badRequest().build();
//        }
//        ReservationResponse reservation = reservationService.save(reservationRequest);
//
//        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
//    }

//@PostMapping("/reservations")
//public ResponseEntity createReservation(@RequestBody ReservationRequest reservationRequest, Member member) {
//    if (member != null) {
//        // 멤버 정보가 존재하면 예약 생성 요청 처리
//        ReservationResponse response = reservationService.save(reservationRequest);
//        if (response != null) {
//            return ResponseEntity.status(HttpStatus.CREATED).body(response);
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create reservation.");
//        }
//    } else {
//        // 멤버 정보가 존재하지 않으면 예약 생성 실패 처리
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create reservation. Invalid member information.");
//    }
//}

//    @PostMapping("/reservations")
//    public ResponseEntity create(@RequestBody ReservationRequest reservationRequest, Member member) {
//        if (reservationRequest.getDate() == null || reservationRequest.getTheme() == null || reservationRequest.getTime() == null) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        // Set name if not provided and member is logged in
//        if (reservationRequest.getName() == null && member != null) {
//            reservationRequest.setName(member.getName());
//        }
//
//        ReservationResponse reservation = reservationService.save(reservationRequest);
//        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
//    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
