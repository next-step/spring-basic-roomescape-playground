package roomescape.reservation.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.auth.AuthorizationException;
import roomescape.member.domain.LoginMember;
import roomescape.member.domain.Member;
import roomescape.member.service.MemberService;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.Time;
import roomescape.time.repository.TimeRepository;

import java.util.List;

@Service
public class ReservationService {
    private static final String DUPLICATE_RESERVATION_MESSAGE = "이미 예약된 날짜, 테마, 시간입니다.";

    private final ReservationRepository reservationRepository;
    private final MemberService memberService;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              MemberService memberService,
                              ThemeRepository themeRepository,
                              TimeRepository timeRepository) {
        this.reservationRepository = reservationRepository;
        this.memberService = memberService;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    @Transactional
    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        validateDuplicateReservation(reservationRequest);
        Member member = findReservationMember(reservationRequest, loginMember);
        Time time = findTime(reservationRequest.timeId());
        Theme theme = findTheme(reservationRequest.themeId());
        Reservation reservation = new Reservation(member, reservationRequest.date(), time, theme);

        try {
            return toResponse(reservationRepository.save(reservation));
        } catch (DataIntegrityViolationException exception) {
            throw new IllegalArgumentException(DUPLICATE_RESERVATION_MESSAGE, exception);
        }
    }

    private void validateDuplicateReservation(ReservationRequest reservationRequest) {
        if (reservationRepository.existsByDateAndThemeIdAndTimeId(
                reservationRequest.date(),
                reservationRequest.themeId(),
                reservationRequest.timeId()
        )) {
            throw new IllegalArgumentException(DUPLICATE_RESERVATION_MESSAGE);
        }
    }

    public void deleteById(Long id, LoginMember loginMember) {
        if (!loginMember.isAdmin()) {
            throw new AuthorizationException("예약을 삭제할 권한이 없습니다.");
        }
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private Member findReservationMember(ReservationRequest reservationRequest, LoginMember loginMember) {
        if (isReservationForAnotherMember(reservationRequest, loginMember)) {
            return memberService.findById(reservationRequest.memberId());
        }
        return memberService.findById(loginMember.id());
    }

    private boolean isReservationForAnotherMember(ReservationRequest reservationRequest, LoginMember loginMember) {
        return loginMember.isAdmin() && reservationRequest.memberId() != null;
    }

    private Time findTime(Long timeId) {
        return timeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("예약 시간을 찾을 수 없습니다."));
    }

    private Theme findTheme(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("예약 테마를 찾을 수 없습니다."));
    }

    private ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getMemberName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue()
        );
    }
}
