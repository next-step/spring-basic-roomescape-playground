package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.login.LoginMember;
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
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository, ThemeRepository themeRepository, TimeRepository timeRepository, MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.memberRepository = memberRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        String name = reservationRequest.getName();
        if (name == null) {
            name = loginMember.name();
        }
        Theme theme = themeRepository.findById(reservationRequest.getTheme())
                .orElseThrow();
        Time time = timeRepository.findById(reservationRequest.getTime())
                .orElseThrow();
        Member member= memberRepository.findByName(name)
                .orElseThrow();
        Reservation reservation = new Reservation(
                member,
                reservationRequest.getDate(),
                time,
                theme
        );
        reservationRepository.save(reservation);
        return new ReservationResponse(
                reservation.getMember(),
                reservation.getName(),
                reservation.getId(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getTimeValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getMember(),it.getName(), it.getId(), it.getTheme().getName(), it.getDate(), it.getTime().getTimeValue()))
                .toList();
    }
}
