package roomescape.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.login.LoginMember;
import roomescape.login.UnauthorizedException;
import roomescape.member.Member;

import java.net.URI;

@RestController
public class WaitingController {
    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> create(@RequestBody WaitingRequest waitingRequest, @LoginMember Member loginMember) {
        if (waitingRequest.getDate() == null
                || waitingRequest.getTheme() == null
                || waitingRequest.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (loginMember == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        WaitingResponse waitingResponse = waitingService.save(waitingRequest, loginMember);
        return ResponseEntity.created(URI.create("/waitings/" + waitingResponse.getId())).body(waitingResponse);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @LoginMember Member loginMember) {
        if (loginMember == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }
        waitingService.deleteById(id, loginMember);
        return ResponseEntity.noContent().build();
    }
}
