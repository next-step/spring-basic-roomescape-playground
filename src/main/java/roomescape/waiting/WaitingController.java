package roomescape.waiting;

import java.net.URI;
import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.loginmember.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRole;

@RestController
public class WaitingController {
    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    private static WaitingRequest checkRequestName(WaitingRequest waitingRequest, Member member) {
        WaitingRequest request;
        if (waitingRequest.name() == null) {
            request = new WaitingRequest(
                    member.getName(),
                    waitingRequest.date(),
                    waitingRequest.theme(),
                    waitingRequest.time()
            );
        } else if (Objects.equals(member.getRole(), MemberRole.ADMIN.toString())) {
            request = waitingRequest;
        } else {
            throw new IllegalArgumentException("관리자 이외에는 자신의 이름으로만 예약할 수 있습니다");
        }
        return request;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> create(@RequestBody WaitingRequest waitingRequest,
                                                  @LoginMember Member member) {
        if (waitingRequest.date() == null
                || waitingRequest.theme() == null
                || waitingRequest.time() == null) {
            return ResponseEntity.badRequest().build();
        }

        WaitingRequest request = checkRequestName(waitingRequest, member);

        WaitingResponse waiting = waitingService.save(request);

        return ResponseEntity.created(URI.create("/waitings/" + waiting.id())).body(waiting);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        waitingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
