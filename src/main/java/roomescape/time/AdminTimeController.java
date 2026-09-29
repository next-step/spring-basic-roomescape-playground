package roomescape.time;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class AdminTimeController {

    private final TimeService timeService;

    public AdminTimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @PostMapping("/admin/times")
    public ResponseEntity<TimeResponse> create(@RequestBody TimeRequest request) {
        if (request.getValue() == null || request.getValue().isEmpty()) {
            throw new IllegalArgumentException();
        }

        TimeResponse newTime = timeService.save(request);

        return ResponseEntity.created(URI.create("/admin/times/" + newTime.getId())).body(newTime);
    }


    @DeleteMapping("/admin/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        timeService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
