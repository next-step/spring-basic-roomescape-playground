package roomescape.reservation;

import org.springframework.stereotype.Service;
import java.util.List;
import roomescape.member.LoginMember;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              TimeRepository timeRepository,
                              ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        String reservationName;
        if (reservationRequest.getName() != null && !reservationRequest.getName().isBlank()) {
            reservationName = reservationRequest.getName();
        } else {
            if (loginMember == null) {
                throw new IllegalArgumentException("로그인 정보가 없거나 예약자 이름이 비어있습니다.");
            }
            reservationName = loginMember.getName();
        }

        Time time = timeRepository.findById(reservationRequest.getTime()).orElseThrow();
        Theme theme = themeRepository.findById(reservationRequest.getTheme()).orElseThrow();
        Long memberId = (loginMember != null) ? loginMember.getId() : null;

        Reservation reservation = new Reservation(reservationName, reservationRequest.getDate(), time, theme, memberId);
        Reservation saved = reservationRepository.save(reservation);

        return new ReservationResponse(saved.getId(), reservationName, saved.getTheme().getName(), saved.getDate(), saved.getTime().getValue());
    }

    public void deleteById(Long id, LoginMember loginMember) {
        if (loginMember == null) {
            throw new IllegalArgumentException("로그인 정보가 없습니다.");
        }

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        if (!"ADMIN".equals(loginMember.getRole()) && !reservation.getName().equals(loginMember.getName())) {
            throw new IllegalArgumentException("본인의 예약만 삭제할 수 있습니다.");
        }

        reservationRepository.delete(reservation);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
