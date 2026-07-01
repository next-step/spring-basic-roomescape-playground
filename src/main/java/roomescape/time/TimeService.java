package roomescape.time;

import org.springframework.stereotype.Service;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationService;
import roomescape.theme.Theme;
import roomescape.theme.ThemeResponse;
import roomescape.theme.ThemeService;

import java.util.List;

@Service
public class TimeService {
    private final TimeRepository timeRepository;
    private final ThemeService themeService;
    private final ReservationService reservationService;

    public TimeService(TimeRepository timeRepository, ThemeService themeService, ReservationService reservationService) {
        this.timeRepository = timeRepository;
        this.themeService = themeService;
        this.reservationService = reservationService;
    }

    public List<AvailableTime> getAvailableTime(String date, Long themeId) {
        Theme theme = themeService.findEntityById(themeId);
        List<Reservation> reservations = reservationService.findEntitiesByDateAndTheme(date, theme);
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

    public Time findEntityById(Long id) {
        return timeRepository.findById(id).orElseThrow(NoSuchTimeException::new);    }

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
