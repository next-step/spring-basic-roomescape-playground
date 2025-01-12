package roomescape.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.DuplicateReservationException;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;

    public ReservationController(ReservationRepository reservationRepository, ReservationService reservationService, MemberRepository memberRepository) {
        this.reservationService = reservationService;
        this.memberRepository = memberRepository;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @GetMapping("/reservations-mine")
    public List<MyReservationResponse> myList(Member loginMember) {
        return reservationService.findMyReservationsAll(loginMember);
    }

    @PostMapping("/reservations")
    public ResponseEntity create(@RequestBody ReservationRequest reservationRequest, Member loginMember) {

        if (reservationRepository.existsByDateAndThemeIdAndTimeId(reservationRequest.getDate(),
                Long.parseLong(reservationRequest.getTheme()),
                Long.parseLong(reservationRequest.getTheme()))) {
            throw new DuplicateReservationException("해당 예약은 이미 예약되어 있습니다.");
        }


        if (reservationRequest.getName() == null) {
            reservationRequest.setName(loginMember.getName());
        }

        if (reservationRequest.getName() == null
                || reservationRequest.getDate() == null
                || reservationRequest.getTheme() == null
                || reservationRequest.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }

        Member member = memberRepository.findByName(reservationRequest.getName())
                .orElseThrow(() -> new IllegalArgumentException("해당 이름을 가진 사용자를 찾을 수 없습니다."));

        reservationRequest.setMemberId(member.getId());

        ReservationResponse reservation = reservationService.save(reservationRequest);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
