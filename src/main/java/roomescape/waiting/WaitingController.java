package roomescape.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.WaitingResponse;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberService;
import roomescape.dto.ReservationRequest;

import java.net.URI;

@RestController
public class WaitingController {
    private final WaitingService waitingService;
    private final MemberService memberService;

    public WaitingController(WaitingService waitingService, MemberService memberService) {
        this.waitingService = waitingService;
        this.memberService = memberService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> create(@RequestBody ReservationRequest request, LoginMember loginMember) {
        Member member = memberService.findById(loginMember.getId());

        WaitingResponse response = waitingService.createWaiting(request, member);
        return ResponseEntity.created(URI.create("/waitings/" + response.id())).body(response);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        waitingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
