package roomescape.time;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.ReservationRepository;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
@RestController
public class TimeController {
    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    public TimeController(TimeRepository timeRepository,ReservationRepository reservationRepository) {
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/times")
    public List<Time> list() {
        return timeRepository.findByDeletedFalse();
    }

    @PostMapping("/times")
    public ResponseEntity<Time> create(@RequestBody Time time) {
        if (time.getValue() == null || time.getValue().isEmpty()) {
            throw new RuntimeException();
        }

        Time newTime = timeRepository.save(time);
        return ResponseEntity.created(URI.create("/times/" + newTime.getId())).body(newTime);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        timeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<AvailableTime>> availableTimes(@RequestParam String date, @RequestParam Long themeId) {
        List<Time> times = timeRepository.findByDeletedFalse();
        List<AvailableTime> result = new ArrayList<>();

        for(Time t:times){
            boolean isBooked = reservationRepository.existsByDateAndTimeAndTheme(date, t.getId(), themeId);
            result.add(new AvailableTime(t.getId(), t.getValue(), isBooked));
        }
        return ResponseEntity.ok(result);
    }
}
