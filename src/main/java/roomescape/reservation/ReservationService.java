package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;

import java.util.List;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, Member member) {
        Time time = new Time(reservationRequest.time());
        Theme theme = new Theme(reservationRequest.themeName(),
                reservationRequest.themeDescription());

        Reservation reservation = reservationRepository.save(
                        new Reservation(
                                reservationRequest.name(),
                                reservationRequest.date(),
                                time,
                                theme
                        )
        );

        return new ReservationResponse(reservation.getId(), member.getName(),
                reservation.getTheme().getName(), reservation.getDate(),
                reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(),
                        it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
