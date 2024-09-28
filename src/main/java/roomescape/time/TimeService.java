package roomescape.time;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;

@Service
@RequiredArgsConstructor
public class TimeService {

    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public List<AvailableTime> getAvailableTime(String date, Long themeId) {
        Theme theme = themeRepository.getById(themeId);
        List<Reservation> reservations = reservationRepository.findByDateAndTheme(date, theme);
        List<Time> times = timeRepository.findAll();

        return times.stream()
            .map(time -> new AvailableTime(
                time.getId(),
                time.getTime(),
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
