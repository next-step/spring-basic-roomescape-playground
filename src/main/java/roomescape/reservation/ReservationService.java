package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,TimeRepository timeRepository,ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository=timeRepository;
        this.themeRepository=themeRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Time time = timeRepository.findById(reservationRequest.getTime()).get();
        Theme theme = themeRepository.findById(reservationRequest.getTheme()).get();
        Reservation reservation=new Reservation(reservationRequest.getName(), reservationRequest.getDate(), time,theme);
        reservationRepository.save(reservation);

        return new ReservationResponse(reservation.getId(), reservationRequest.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getEvent_value());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getEvent_value()))
                .toList();
    }
}
