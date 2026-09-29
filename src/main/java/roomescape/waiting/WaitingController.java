package roomescape.waiting;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.login.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.ReservationRequest;

@RestController
public class WaitingController {

    private final WaitingService waitingService;
    private final MemberRepository memberRepository;

    public WaitingController(WaitingService waitingService, MemberRepository memberRepository) {
        this.waitingService = waitingService;
        this.memberRepository = memberRepository;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> create(@RequestBody ReservationRequest request, LoginMember loginMember) {

        if (request.getDate() == null
                || request.getTheme() == null
                || request.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }

        Member member = memberRepository.findById(loginMember.getId()).orElseThrow();
        WaitingResponse response = waitingService.save(request, member);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



}
