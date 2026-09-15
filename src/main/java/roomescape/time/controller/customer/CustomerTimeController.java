package roomescape.time.controller.customer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.time.AvailableTime;
import roomescape.time.Time;
import roomescape.time.TimeService;

import java.util.List;

@RestController
public class CustomerTimeController {
    private final TimeService timeService;

    public CustomerTimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping("/times")
    public List<Time> list() {
        return timeService.findAll();
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<AvailableTime>> availableTimes(@RequestParam String date, @RequestParam Long themeId) {
        return ResponseEntity.ok(timeService.getAvailableTime(date, themeId));
    }
}
