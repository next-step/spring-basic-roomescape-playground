package roomescape.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.Authentication;
import roomescape.member.Member;
import roomescape.waiting.dto.WaitingRequest;
import roomescape.waiting.dto.WaitingResponse;

import java.net.URI;

@RestController
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity create(@Authentication Member member,
                                 @RequestBody WaitingRequest waitingRequest) {
        if (waitingRequest.getDate() == null || waitingRequest.getTheme() == null || waitingRequest.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (waitingRequest.getName() == null) {
            waitingRequest = new WaitingRequest(member.getName(), waitingRequest.getDate(), waitingRequest.getTheme(), waitingRequest.getTime()
            );
        }
        WaitingResponse waiting = waitingService.save(waitingRequest);
        return ResponseEntity.created(URI.create("/waitings/" + waiting.getId())).body(waiting);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        waitingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
