package roomescape.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.auth.LoginMember;

import java.net.URI;

@Controller
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity create(@RequestBody WaitingRequest waitingRequest, LoginMember loginMember) {
        if (
                waitingRequest.date() == null
                        || waitingRequest.themeId() == null
            return ResponseEntity.badRequest().build();
        }

        WaitingResponse response = waitingService.save(waitingRequest, loginMember);

        return ResponseEntity.created(URI.create("/waitings/" + response.id())).body(response);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        waitingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
