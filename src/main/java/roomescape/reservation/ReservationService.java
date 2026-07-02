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
        Theme theme = themeRepository.findById(reservationRequest.getTheme()).orElseThrow();
        Time time = timeRepository.findById(reservationRequest.getTime()).orElseThrow();
        Reservation reservation = reservationRepository.save(createReservation(reservationRequest, loginMember, theme, time));

        return new ReservationResponse(
                reservation.getId(),
                getReservationName(reservation),
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
                        getReservationName(it),
                        it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getValue()
                ))
                .toList();
    }

    public List<MyReservationResponse> findMine(LoginMember loginMember) {
        if (loginMember == null) {
            throw new UnauthorizedException();
        }

        return reservationRepository.findByMemberId(loginMember.getId()).stream()
                .map(it -> new MyReservationResponse(
                        it.getId(),
                        it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getValue(),
                        "예약"
                ))
                .toList();
    }

    private Reservation createReservation(
            ReservationRequest reservationRequest,
            LoginMember loginMember,
            Theme theme,
            Time time
    ) {
        if (hasName(reservationRequest)) {
            return new Reservation(reservationRequest.getName(), reservationRequest.getDate(), time, theme);
        }

        if (loginMember == null) {
            throw new UnauthorizedException();
        }

        Member member = memberRepository.findById(loginMember.getId()).orElseThrow();
        return new Reservation("", reservationRequest.getDate(), member, time, theme);
    }

    private boolean hasName(ReservationRequest reservationRequest) {
        return reservationRequest.getName() != null
                && !reservationRequest.getName().isBlank();
    }

    private String getReservationName(Reservation reservation) {
        if (reservation.getName() != null && !reservation.getName().isBlank()) {
            return reservation.getName();
        }

        return reservation.getMember().getName();
    }
}
