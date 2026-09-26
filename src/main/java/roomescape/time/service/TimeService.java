package roomescape.time.service;

import org.springframework.stereotype.Service;
import roomescape.time.domain.Time;
import roomescape.time.repository.TimeRepository;

import java.time.LocalTime;
import java.util.List;

@Service
public class TimeService {
    private final TimeRepository timeRepository;

    public TimeService(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public List<TimeResult> findAll() {
        return timeRepository.findAll().stream()
                .map(this::toResult)
                .toList();
    }

    public TimeResult save(LocalTime value) {
        return toResult(timeRepository.save(new Time(value)));
    }

    public void deleteById(Long id) {
        timeRepository.deleteById(id);
    }

    private TimeResult toResult(Time time) {
        return new TimeResult(time.getId(), time.getValue());
    }
}
