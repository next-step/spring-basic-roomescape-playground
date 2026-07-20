package roomescape.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.AuthUser;
import roomescape.auth.LoginMemberInfo;

import java.net.URI;

@RestController
public class WaitingController {
    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> create(@RequestBody WaitingRequest request,
                                                   @AuthUser LoginMemberInfo loginMember) {
        WaitingResponse waiting = waitingService.save(request, loginMember);
        return ResponseEntity.created(URI.create("/waitings/" + waiting.getId())).body(waiting);
    }
}
