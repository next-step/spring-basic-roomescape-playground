package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.member.Member;
import roomescape.domain.waiting.WaitingRequest;
import roomescape.domain.waiting.WaitingResponse;
import roomescape.domain.waiting.WaitingService;

import java.net.URI;

@RestController
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity create(@RequestBody WaitingRequest waitingRequest, Member member) {

        WaitingResponse waitingResponse = waitingService.save(waitingRequest, member);

        return ResponseEntity.created(URI.create("/waitings/" + waitingResponse.id()))
                .body(waitingResponse);
    }

    @PostMapping("/waitings/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        waitingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
