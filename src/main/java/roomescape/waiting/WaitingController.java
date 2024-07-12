package roomescape.waiting;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.AuthSession;
import roomescape.member.LoginMember;

@RestController
public class WaitingController {
    @Autowired
    private WaitingService waitingService;

    @PostMapping("/waitings")
    public ResponseEntity create(@RequestBody WaitingRequest request,@AuthSession LoginMember loginMember) {
        WaitingResponse waiting = waitingService.save(request, loginMember.getId());
        return ResponseEntity.created(URI.create("/waitings/" + waiting.getId())).body(waiting);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        waitingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
