package roomescape.time;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.time.dto.TimeRequest;

import java.net.URI;
import java.util.List;

@RestController
public class TimeController {

    private TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping("/times")
    public ResponseEntity<List<Time>> list() {
        return ResponseEntity.ok(timeService.findAll());
    }

    @PostMapping("/times")
    public ResponseEntity<Time> create(@RequestBody TimeRequest request) {
        Time newTime = timeService.save(new Time(request.getTime()));
        return ResponseEntity.created(URI.create("/times/" + newTime.getId())).body(newTime);
    }

    @GetMapping("/times/{id}")
    public ResponseEntity<Time> findTimeById(@PathVariable Long id) {
        Time time = timeService.findById(id);
        return ResponseEntity.ok(time);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        timeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<AvailableTime>> availableTimes(@RequestParam String date, @RequestParam Long themeId) {
        return ResponseEntity.ok(timeService.getAvailableTime(date, themeId));
    }
}
