package roomescape.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.theme.Theme;
import roomescape.theme.ThemeService;
import roomescape.time.Time;
import roomescape.time.TimeService;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ThemeService themeService;
    private final TimeService timeService;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, ThemeService themeService, TimeService timeService) {
        this.reservationRepository = reservationRepository;
        this.themeService = themeService;
        this.timeService = timeService;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Theme theme = themeService.findEntityById(reservationRequest.themeId());
        Time time = timeService.findEntityById(reservationRequest.timeId());
        Reservation reservation = reservationRepository.save(new Reservation(reservationRequest.name(), reservationRequest.date(), time, theme));

        return new ReservationResponse(reservation.getId(), reservationRequest.name(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<Reservation> findEntitiesByDateAndTheme(String date, Theme theme) {
        return reservationRepository.findByDateAndTheme(date, theme);
    }
}
