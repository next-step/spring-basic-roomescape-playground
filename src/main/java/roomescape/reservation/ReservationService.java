package roomescape.reservation;

import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationStatus;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;

import java.util.List;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.Time;
import roomescape.time.repository.TimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
            TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, Member member) {
        Time time = timeRepository.findById(reservationRequest.timeId())
                .orElseThrow(() -> new NoSuchElementException("time not found"));
        Theme theme = themeRepository.findById(reservationRequest.themeId())
                .orElseThrow(() -> new NoSuchElementException("theme not found"));

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

    public List<MyReservationResponse> findByMember(Member member) {
        return reservationRepository.findByMemberId(member.getId()).stream()
                .map(r -> new MyReservationResponse(r.getId(), r.getTheme().getName(),
                        r.getDate(), r.getTime().getValue(), ReservationStatus.RESERVATION))
                .toList();
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
