package roomescape.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.member.MemberService;

import java.net.URI;

@RestController
public class WaitingController {
    private final WaitingService waitingService;
    private final MemberService memberService;

    public WaitingController(WaitingService waitingService,
                             MemberService memberService) {
        this.waitingService = waitingService;
        this.memberService = memberService;
    }

    @PostMapping("/waitings")
    public ResponseEntity create(@RequestBody WaitingRequest waitingRequest, LoginMember loginMember) {
        if (waitingRequest.date() == null
                || waitingRequest.theme() == null
                || waitingRequest.time() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (loginMember == null) {
            return ResponseEntity.status(401).build();
        }

        Member member = memberService.findById(loginMember.id());
        WaitingResponse waiting = waitingService.save(waitingRequest, member);

        return ResponseEntity.created(URI.create("/waitings/" + waiting.id())).body(waiting);
    }
}
