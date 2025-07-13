package roomescape.time;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TimeService {

    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public TimeService(TimeRepository timeRepository, ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<AvailableTime> getAvailableTime(String date, Long themeId) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        List<Reservation> reservations = reservationRepository.findByDateAndTheme(localDate, theme);
        List<Time> allTimes = timeRepository.findAll();

        Set<Time> reservedTimes = reservations.stream()
                .map(Reservation::getTime)
                .collect(Collectors.toSet());

        return allTimes.stream()
                .map(time -> new AvailableTime(time.getId(), time.getTime(), reservedTimes.contains(time)))
                .toList();
    }

    public List<Time> findAll() {
        return timeRepository.findAll();
    }

    public Time findById(Long id) {
        return timeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));
    }

    @Transactional
    public Time save(Time time) {
        return timeRepository.save(time);
    }

    @Transactional
    public void deleteById(Long id) {
        timeRepository.deleteById(id);
    }
}
