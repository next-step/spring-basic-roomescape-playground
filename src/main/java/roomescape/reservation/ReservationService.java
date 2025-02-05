package roomescape.reservation;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.theme.Theme;
import roomescape.theme.ThemeService;
import roomescape.time.Time;
import roomescape.time.TimeService;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeService timeService;
    private final ThemeService themeService;

    public ReservationService(ReservationRepository reservationRepository, TimeService timeService,
                              ThemeService themeService) {
        this.reservationRepository = reservationRepository;
        this.timeService = timeService;
        this.themeService = themeService;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Time time = timeService.findByIdOrThrow(reservationRequest.getTime());
        Theme theme = themeService.findByIdOrThrow(reservationRequest.getTime());
        Reservation reservation = new Reservation(reservationRequest.getName(), reservationRequest.getDate(), time,
                theme);

        Reservation savedReservation = reservationRepository.save(reservation);

        return new ReservationResponse(savedReservation.getId(), savedReservation.getName(),
                savedReservation.getTheme().getName(), savedReservation.getDate(),
                savedReservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(),
                        it.getTime().getValue()))
                .toList();
    }
}
