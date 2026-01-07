package roomescape.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.member.LoginMemberDto;

import java.net.URI;
import java.util.Map;

@RestController
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

	@PostMapping("/waitings")
	public ResponseEntity<WaitingResponseDto> create(@RequestBody Map<String, String> body, LoginMemberDto member) {
        String date = body.get("date");
        Long timeId = Long.valueOf(body.get("time"));
        Long themeId = Long.valueOf(body.get("theme"));
		WaitingResponseDto waiting = waitingService.create(member.getId(), date, timeId, themeId);
        return ResponseEntity.created(URI.create("/waitings/" + waiting.getId())).body(waiting);
    }

    @DeleteMapping("/waitings/{id}")
	public ResponseEntity<Void> cancel(@PathVariable Long id, LoginMemberDto member) {
        waitingService.cancel(id, member.getId());
        return ResponseEntity.noContent().build();
    }
}


