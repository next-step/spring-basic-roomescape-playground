package roomescape.reservation;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final MemberRepository memberRepository;

    public ReservationResponse save(ReservationRequest request, LoginMember loginMember) {
        String name = Optional.ofNullable(request.name()).orElse(loginMember.name());
        Theme theme = themeRepository.getById(request.theme());
        Time time = timeRepository.getById(request.time());
        Member member = memberRepository.getById(loginMember.id());
        Reservation reservation = reservationRepository.save(
            Reservation.builder()
            .name(name)
            .date(request.date())
            .theme(theme)
            .time(time)
            .member(member)
            .build()
        );
        return new ReservationResponse(
            reservation.getId(),
            request.name(),
            reservation.getTheme().getName(),
            reservation.getDate(),
            reservation.getTime().getTime()
        );
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
            .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(),
                it.getTime().getTime()))
            .toList();
    }

    public List<MyReservationResponse> findMyReservations(LoginMember loginMember) {
        Member member = memberRepository.getById(loginMember.id());
        return reservationRepository.findAllByMember(member).stream()
            .map(MyReservationResponse::from)
            .toList();
    }
}
