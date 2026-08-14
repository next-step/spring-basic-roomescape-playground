package roomescape.time;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.ReservationRepository;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
@RestController
public class TimeController {
    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping("/times")
    public List<Time> list() {
        return timeService.findAllActiveTimes();
    }

    @PostMapping("/times")
    public ResponseEntity<Time> create(@RequestBody Time time) {
        Time newTime = timeService.createTime(time);
        return ResponseEntity.created(URI.create("/times/" + newTime.getId())).body(newTime);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        timeService.deleteTime(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<AvailableTime>> availableTimes(@RequestParam String date, @RequestParam Long themeId) {
        List<AvailableTime> result = timeService.getAvailableTimes(date, themeId);
        return ResponseEntity.ok(result);
    }
}
