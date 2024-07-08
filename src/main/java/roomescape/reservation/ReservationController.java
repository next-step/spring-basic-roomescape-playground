package roomescape.reservation;

import auth.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.Waiting.WaitingService;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final MemberService memberService;
    private final WaitingService waitingService;
    private final JwtUtils jwtUtils;

    public ReservationController(ReservationService reservationService,MemberService memberService,WaitingService waitingService,JwtUtils jwtUtils) {
        this.reservationService = reservationService;
        this.memberService=memberService;
        this.waitingService=waitingService;
        this.jwtUtils=jwtUtils;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @PostMapping("/reservations")
    public ResponseEntity create(@RequestBody ReservationRequest reservationRequest, LoginMember member) {
        if (member == null
                || reservationRequest.getDate() == null
                || reservationRequest.getTheme() == null
                || reservationRequest.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }

        String name = reservationRequest.getName();
        if (name == null) {
            name = member.getName();
        }
        CreateReservationDto reservationDto = new CreateReservationDto(member.getId(), name,
                reservationRequest.getDate(), reservationRequest.getTheme(), reservationRequest.getTime());

        ReservationResponse reservation = reservationService.save(reservationDto);


        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations-mine")
    public ResponseEntity reservationsMine(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = jwtUtils.extractTokenFromCookie(cookies);
        Member member = jwtUtils.extractMemberFromToken(token);

        List<MyReservationResponse> reservations = reservationService.findByMemberId(member.getId());
        List<MyReservationResponse> waitings = waitingService.findByMemberId(member.getId());

        List<MyReservationResponse> responses = new ArrayList<>();
        responses.addAll(reservations);
        responses.addAll(waitings);

        return ResponseEntity.ok().body(responses);
    }
}