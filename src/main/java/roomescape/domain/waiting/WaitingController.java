package roomescape.domain.waiting;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.member.dto.LoginMember;
import roomescape.domain.waiting.dto.WaitingRequest;
import roomescape.domain.waiting.dto.WaitingResponse;

@RestController
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(final WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> create(@RequestBody WaitingRequest waitingRequest,
                                                  LoginMember loginMember) {
        final WaitingResponse response = waitingService.save(waitingRequest, loginMember);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        waitingService.deleteWaiting(id);
        return ResponseEntity.noContent().build();
    }
}
