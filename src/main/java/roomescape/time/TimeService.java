package roomescape.time;

import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;

import java.util.List;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

@Service
public class TimeService {
    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public TimeService(TimeRepository timeRepository, ReservationRepository reservationRepository, ThemeRepository themeRepository) {
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    public List<AvailableTime> getAvailableTime(String date, Long themeId) {
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 테마가 없습니다."));
        List<Reservation> reservations = reservationRepository.findByDateAndTheme(date, theme);

        List<Time> times = timeRepository.findAll();

        return times.stream()
                .map(time -> new AvailableTime(
                        time.getId(),
                        time.getValue(),
                        reservations.stream()
                                .anyMatch(reservation -> reservation.getTime().getId().equals(time.getId()))
                ))
                .toList();
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
