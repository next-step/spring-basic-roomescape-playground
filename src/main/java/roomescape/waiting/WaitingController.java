package roomescape.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.LoginMember;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> waiting(LoginMember loginMember, @RequestBody WaitingRequest waitingRequest) throws MalformedURLException, URISyntaxException {

        Long rank = waitingService.create(loginMember.getId(), waitingRequest);

        return ResponseEntity
                .created(URI.create("/"))
                .body(new WaitingResponse(rank));
    }
}
