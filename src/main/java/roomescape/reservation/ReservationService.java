package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.MemberRepository;
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
    private final MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository, ThemeRepository themeRepository, MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }
    public ReservationResponse save(ReservationRequest reservationRequest) {
        Time time = timeRepository.findByValue(reservationRequest.getTime()).orElseThrow(() -> new IllegalArgumentException("없는 시간입니다."));
        Theme theme = themeRepository.findByName(reservationRequest.getTheme()).orElseThrow(() -> new IllegalArgumentException("없는 테마입니다."));

        Reservation reservation = new Reservation(reservationRequest.getName(), reservationRequest.getDate(), time, theme, null);
        reservationRepository.save(reservation);
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
