package roomescape.waiting;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WaitingController {

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> create(@RequestBody WaitingRequest waitingRequest) {
        return ResponseEntity.created(URI.create("ASDSAD")).body(null);
    }
}
