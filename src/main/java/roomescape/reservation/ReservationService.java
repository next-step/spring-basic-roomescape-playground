package roomescape.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        if (reservationRequest.getName() == null || reservationRequest.getName().isBlank()) {
            reservationRequest = new ReservationRequest(loginMember.name(), reservationRequest.getDate(), reservationRequest.getTheme(), reservationRequest.getTime());
        }

        validateDuplicatedReservation(reservationRequest, loginMember);

        Reservation reservation = new Reservation(reservationRequest.getName(),
                reservationRequest.getDate(),
                timeRepository.findById(reservationRequest.getTime()).orElseThrow(()->new RoomEscapeException(TIME_NOT_FOUND)),
                themeRepository.findById(reservationRequest.getTheme()).orElseThrow(()->new RoomEscapeException(THEME_NOT_FOUND)),
                memberRepository.findById(loginMember.id()).orElseThrow(() -> new RoomEscapeException(MEMBER_NOT_FOUND)));

        reservationRepository.save(reservation);
        return new ReservationResponse(reservation.getId(), reservationRequest.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getTime());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getTime()))
                .toList();
    }

    public List<MyReservationResponse> findReservationByMember(LoginMember loginMember) {
        return reservationRepository.findByMemberId(loginMember.id()).stream()
                .map(MyReservationResponse::from)
                .toList();
    }

    private void validateDuplicatedReservation(ReservationRequest reservationRequest, LoginMember loginMember) {
        boolean alreadyReserved = reservationRepository.existsByMemberIdAndThemeIdAndDateAndTimeId(
                loginMember.id(),
                reservationRequest.getTheme(),
                reservationRequest.getDate(),
                reservationRequest.getTime());
        if (alreadyReserved) {
            throw new RoomEscapeException(DUPLICATE_RESERVATION);
        }
    }
}
