package roomescape.reservation;

import io.jsonwebtoken.Jwts;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.member.Member;
import roomescape.JwtTokenProvider;
import roomescape.member.MemberService;
import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public ReservationController(ReservationService reservationService, MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.reservationService = reservationService;
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @PostMapping("/reservations")
    public ResponseEntity create(@RequestBody ReservationRequest reservationRequest, @CookieValue(value = "token", required = false) String token) {
        if (reservationRequest.getDate() == null
                || reservationRequest.getTheme() == null
                || reservationRequest.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (reservationRequest.getName() == null) {

            String memberId = jwtTokenProvider.getMemberId(token);

            Member member = memberService.findById(Integer.parseInt(memberId));
            reservationRequest.setName(member.getName());
        }

        ReservationResponse reservation = reservationService.save(reservationRequest);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.id())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
