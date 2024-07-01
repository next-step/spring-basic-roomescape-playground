package roomescape.time;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class TimeDao {
    private final TimeRepository timeRepository;

    public List<Time> findAll() {
        return timeRepository.findAll();
    }

    public Time save(Time time) {
        return timeRepository.save(time);
    }

    public void deleteById(Long id) {
        timeRepository.deleteById(id);
    }

    public Time findById(Long id) {
        return timeRepository.findById(id).orElseThrow();
    }
}
