package roomescape.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.login.LoginMember;

import java.net.URI;

@RestController
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> create(@RequestBody WaitingRequest request, LoginMember loginMember) {
        if (loginMember == null) {
            return ResponseEntity.status(401).build();
        }
        WaitingResponse response = waitingService.save(request, loginMember.getId());
        return ResponseEntity.created(URI.create("/waitings/" + response.getId())).body(response);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, LoginMember loginMember) {
        if (loginMember == null) {
            return ResponseEntity.status(401).build();
        }
        waitingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
