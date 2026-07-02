package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;
import roomescape.auth.UnauthorizedException;
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
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            MemberRepository memberRepository,
            ThemeRepository themeRepository,
            TimeRepository timeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        Member member = findReservationMember(reservationRequest, loginMember);
        Theme theme = themeRepository.findById(reservationRequest.getTheme()).orElseThrow();
        Time time = timeRepository.findById(reservationRequest.getTime()).orElseThrow();
        Reservation reservation = reservationRepository.save(
                new Reservation(member.getName(), reservationRequest.getDate(), time, theme)
        );

        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue()
        );
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(
                        it.getId(),
                        it.getName(),
                        it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getValue()
                ))
                .toList();
    }

    private Member findReservationMember(ReservationRequest reservationRequest, LoginMember loginMember) {
        if (hasName(reservationRequest)) {
            return memberRepository.findByName(reservationRequest.getName()).orElseThrow();
        }

        if (loginMember == null) {
            throw new UnauthorizedException();
        }

        return memberRepository.findById(loginMember.getId()).orElseThrow();
    }

    private boolean hasName(ReservationRequest reservationRequest) {
        return reservationRequest.getName() != null
                && !reservationRequest.getName().isBlank();
    }
}
