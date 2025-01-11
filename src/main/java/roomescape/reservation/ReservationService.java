package roomescape.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
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

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getTime()))
                .toList();
    }

    public List<MyReservationResponse> findMyReservationsAll(Member loginMember) {
        return reservationRepository.findByName(loginMember.getName()).stream()
                .map(it -> new MyReservationResponse(it.getId(), it.getTheme().getName(), it.getDate(), it.getTime().getTime(), "예약"))
                .toList();
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Time time = timeRepository.findById(Long.parseLong(reservationRequest.getTime()))
                .orElseThrow(() -> new IllegalArgumentException("해당 시간이 존재하지 않습니다."));

        Theme theme = themeRepository.findById(Long.parseLong(reservationRequest.getTheme()))
                .orElseThrow(() -> new IllegalArgumentException("해당 테마가 존재하지 않습니다."));

        Reservation reservation = reservationRepository.save(new Reservation(
                reservationRequest.getName(),
                reservationRequest.getDate(),
                time,
                theme,
                null));

        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getTime());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}
