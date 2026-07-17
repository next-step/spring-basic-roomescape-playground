package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.login.LoginMember;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public ReservationService(ReservationRepository reservationRepository,ThemeRepository themeRepository,TimeRepository timeRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository=themeRepository;
        this.timeRepository=timeRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        String name = reservationRequest.getName();
        if (name == null) {
            name = loginMember.name();
        }
        Theme theme = themeRepository.findById(reservationRequest.getTheme())
                        .orElseThrow();
        Time time=timeRepository.findById(reservationRequest.getTime())
                        .orElseThrow();
        Reservation reservation =new Reservation(
                name,
                reservationRequest.getDate(),
                time,
                theme
        );
        reservationRepository.save(reservation);
        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getTime_value());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getTime_value()))
                .toList();
    }
}
