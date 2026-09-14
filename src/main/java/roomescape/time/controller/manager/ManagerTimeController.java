package roomescape.time.controller.manager;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.time.Time;
import roomescape.time.TimeService;

import java.net.URI;

@RestController
public class ManagerTimeController {
    private final TimeService timeService;

    public ManagerTimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @PostMapping("/manager/times")
    public ResponseEntity<Time> create(@RequestBody Time time) {
        if (time.getValue() == null || time.getValue().isEmpty()) {
            throw new RuntimeException();
        }

        Time newTime = timeService.save(time);
        return ResponseEntity.created(URI.create("/manager/times/" + newTime.getId())).body(newTime);
    }

    @DeleteMapping("/manager/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        timeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
