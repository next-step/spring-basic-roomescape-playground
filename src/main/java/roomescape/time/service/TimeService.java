package roomescape.time.service;

import org.springframework.stereotype.Service;
import roomescape.time.domain.Time;
import roomescape.time.repository.TimeRepository;

import java.util.List;

@Service
public class TimeService {
    private final TimeRepository timeRepository;

    public TimeService(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public List<Time> findAll() {
        return timeRepository.findAll();
    }

    public Time save(Time time) {
        return timeRepository.save(time);
    }

    public void deleteById(Long id) {
        timeRepository.deleteById(id);
    }
}
