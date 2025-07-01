package roomescape.reservation;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import roomescape.exception.ThemeNotFoundException;
import roomescape.exception.TimeNotFoundException;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository,
                              ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Time time = timeRepository.findById(reservationRequest.time())
                .orElseThrow(TimeNotFoundException::new);
        Theme theme = themeRepository.findById(reservationRequest.theme())
                .orElseThrow(ThemeNotFoundException::new);
        Reservation reservation = new Reservation(reservationRequest.name(), reservationRequest.date(), time, theme);

        Reservation savedReservation = reservationRepository.save(reservation);

        return new ReservationResponse(
                savedReservation.getId(),
                savedReservation.getName(),
                savedReservation.getTheme().getName(),
                savedReservation.getDate(),
                savedReservation.getTime().getTimeValue()
        );
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getTimeValue()))
                .toList();
    }
}
