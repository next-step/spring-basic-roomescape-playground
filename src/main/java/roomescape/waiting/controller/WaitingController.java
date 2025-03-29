package roomescape.waiting.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.auth.dto.LoginMember;
import roomescape.waiting.dto.request.WaitingRequest;
import roomescape.waiting.dto.response.WaitingResponse;
import roomescape.waiting.service.WaitingService;

import java.net.URI;

@Controller
public class WaitingController {

    private static final String LOCATION_DEFAULT_VALUE = "/waitings/";

    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> createWaiting(@RequestBody WaitingRequest waitingRequest, LoginMember loginMember) {
        WaitingResponse waiting = waitingService.createWaiting(waitingRequest, loginMember);
        return ResponseEntity.created(URI.create(LOCATION_DEFAULT_VALUE + waiting.id()))
                .body(waiting);
    }
}
