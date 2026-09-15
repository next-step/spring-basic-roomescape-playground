package roomescape.time;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.exception.InvalidTimeException;

import java.net.URI;
import java.util.List;

@RestController
public class TimeController {
    private static final int MAX_TIME_LENGTH = 20;
    private TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping("/times")
    public List<Time> list() {
        return timeService.findAll();
    }

    @PostMapping("/times")
    public ResponseEntity<Time> create(@RequestBody Time time) {
        if (time.getValue() == null
                || time.getValue().isBlank()
                || time.getValue().length() > MAX_TIME_LENGTH) {
            throw new InvalidTimeException("시간을 올바르게 입력해야 합니다.");
        }

        Time newTime = timeService.save(time);
        return ResponseEntity.created(URI.create("/times/" + newTime.getId())).body(newTime);
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