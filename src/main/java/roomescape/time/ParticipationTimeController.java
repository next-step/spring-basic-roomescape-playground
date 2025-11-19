package roomescape.time;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class ParticipationTimeController {
    private ParticipationTimeService participationTimeService;

    public ParticipationTimeController(ParticipationTimeService participationTimeService) {
        this.participationTimeService = participationTimeService;
    }

    @GetMapping("/times")
    public List<ParticipationTime> list() {
        return participationTimeService.findAll();
    }

    @PostMapping("/times")
    public ResponseEntity<ParticipationTime> create(@RequestBody ParticipationTime participationTime) {

        if (participationTime.getTime() == null || participationTime.getTime().isBlank()) {
            throw new IllegalArgumentException("참여 시간은 필수 입력 항목이며 공백일 수 없습니다.");
        }

        ParticipationTime newParticipationTime = participationTimeService.save(participationTime);
        return ResponseEntity.created(URI.create("/times/" + newParticipationTime.getId())).body(newParticipationTime);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        participationTimeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<AvailableTime>> availableTimes(@RequestParam String date, @RequestParam Long themeId) {
        return ResponseEntity.ok(participationTimeService.getAvailableTime(date, themeId));
    }
}
