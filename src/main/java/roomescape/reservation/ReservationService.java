package roomescape.reservation;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import roomescape.auth.LoginMember;
import roomescape.exception.RoomEscapeException;
import roomescape.member.MemberRepository;
import roomescape.theme.ThemeRepository;
import roomescape.time.TimeRepository;

import java.util.List;

import static roomescape.exception.ErrorCode.*;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public ReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository, ThemeRepository themeRepository, TimeRepository timeRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        reservationRequest = fillMissingNameWithLoginMember(reservationRequest, loginMember);
        validateDuplicatedReservation(reservationRequest, loginMember);

        Reservation reservation = toReservation(reservationRequest, loginMember);
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(savedReservation);
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<MyReservationResponse> findReservationByMember(LoginMember loginMember) {
        return reservationRepository.findByMemberId(loginMember.id()).stream()
                .map(MyReservationResponse::from)
                .toList();
    }

    private Reservation toReservation(ReservationRequest reservationRequest, LoginMember loginMember) {
        return new Reservation(reservationRequest.name(),
                reservationRequest.date(),
                timeRepository.findById(reservationRequest.time()).orElseThrow(() -> new RoomEscapeException(TIME_NOT_FOUND)),
                themeRepository.findById(reservationRequest.theme()).orElseThrow(() -> new RoomEscapeException(THEME_NOT_FOUND)),
                memberRepository.findById(loginMember.id()).orElseThrow(() -> new RoomEscapeException(MEMBER_NOT_FOUND)));
    }

    private void validateDuplicatedReservation(ReservationRequest reservationRequest, LoginMember loginMember) {
        boolean alreadyReserved = reservationRepository.existsThemeIdAndDateAndTimeId(
                reservationRequest.theme(),
                reservationRequest.date(),
                reservationRequest.time());
        if (alreadyReserved) {
            throw new RoomEscapeException(DUPLICATE_RESERVATION);
        }
    }

    private static ReservationRequest fillMissingNameWithLoginMember(ReservationRequest reservationRequest, LoginMember loginMember) {
        if (!StringUtils.hasText(reservationRequest.name())) {
            reservationRequest = reservationRequest.withDefaultName(loginMember.name());
        }
        return reservationRequest;
    }
}
