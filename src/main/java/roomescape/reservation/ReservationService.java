package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              MemberRepository memberRepository,
                              TimeRepository timeRepository,
                              ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        String name = resolveName(reservationRequest, loginMember);
        Time time = timeRepository.findById(reservationRequest.getTime())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));
        Theme theme = themeRepository.findById(reservationRequest.getTheme())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        Reservation reservation = reservationRepository.save(
                new Reservation(name, reservationRequest.getDate(), time, theme));

        return new ReservationResponse(reservation.getId(), name, theme.getName(), reservation.getDate(), time.getTime());
    }

    private String resolveName(ReservationRequest reservationRequest, LoginMember loginMember) {
        if (reservationRequest.getName() != null) {
            Member member = memberRepository.findByName(reservationRequest.getName())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
            return member.getName();
        }
        return loginMember.getName();
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getTime()))
                .toList();
    }
}
