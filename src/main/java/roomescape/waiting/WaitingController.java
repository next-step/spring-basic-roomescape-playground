package roomescape.waiting;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.login.LoginMember;
import roomescape.member.Member;

@RestController
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> create(@RequestBody WaitingRequest request, @LoginMember Member member) {
        if (request.getName() == null && member != null) {
            request.setName(member.getName());
        }
        if (request.getName() == null
            || request.getDate() == null
            || request.getTheme() == null
            || request.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }
        WaitingResponse waiting = waitingService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(waiting);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        waitingService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
