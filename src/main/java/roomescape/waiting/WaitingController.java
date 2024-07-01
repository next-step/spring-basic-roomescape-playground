package roomescape.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.member.dto.LoginMember;
import roomescape.waiting.dto.WaitingRequest;
import roomescape.waiting.dto.WaitingResponse;

import java.net.URI;

@Controller
public class WaitingController {
    private WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity createWaiting(@RequestBody WaitingRequest request, LoginMember loginMember) {
        WaitingResponse waitingResponse = waitingService.save(request, loginMember);
        return ResponseEntity.created(URI.create("/waitings/" + waitingResponse.id())).body(waitingResponse);
    }
}
