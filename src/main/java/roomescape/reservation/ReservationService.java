package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.dto.LoginMember;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;
    private TimeRepository timeRepository;
    private ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {

        Optional<Time> timeOptional = timeRepository.findById(reservationRequest.getTime());
        Optional<Theme> themeOptional = themeRepository.findById(reservationRequest.getTheme());

        if (timeOptional.isEmpty() || themeOptional.isEmpty()) return null;

        Reservation reservation= reservationRepository.save(new Reservation(reservationRequest.getName(), reservationRequest.getDate(),timeOptional.get(), themeOptional.get()));
        return new ReservationResponse(reservation.getId(), reservationRequest.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
