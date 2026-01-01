package roomescape.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.member.LoginMember;

import java.net.URI;
import java.util.Map;

@RestController
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> create(@RequestBody Map<String, String> body, LoginMember member) {
        String date = body.get("date");
        Long timeId = Long.valueOf(body.get("time"));
        Long themeId = Long.valueOf(body.get("theme"));
        WaitingResponse waiting = waitingService.create(member.getId(), date, timeId, themeId);
        return ResponseEntity.created(URI.create("/waitings/" + waiting.getId())).body(waiting);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id, LoginMember member) {
        waitingService.cancel(id, member.getId());
        return ResponseEntity.noContent().build();
    }
}


