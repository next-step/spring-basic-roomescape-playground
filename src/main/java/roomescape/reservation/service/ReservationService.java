package roomescape.reservation.service;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.auth.AuthorizationException;
import roomescape.member.domain.LoginMember;
import roomescape.member.domain.Member;
import roomescape.member.service.MemberService;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.repository.ReservationDao;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeDao;
import roomescape.time.domain.Time;
import roomescape.time.repository.TimeDao;

import java.util.List;

@Service
public class ReservationService {
    private static final String DUPLICATE_RESERVATION_MESSAGE = "이미 예약된 날짜, 테마, 시간입니다.";

    private final ReservationDao reservationDao;
    private final MemberService memberService;
    private final ThemeDao themeDao;
    private final TimeDao timeDao;

    public ReservationService(ReservationDao reservationDao,
                              MemberService memberService,
                              ThemeDao themeDao,
                              TimeDao timeDao) {
        this.reservationDao = reservationDao;
        this.memberService = memberService;
        this.themeDao = themeDao;
        this.timeDao = timeDao;
    }

    @Transactional
    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        validateDuplicateReservation(reservationRequest);
        Member member = findReservationMember(reservationRequest, loginMember);
        Time time = findTime(reservationRequest.timeId());
        Theme theme = findTheme(reservationRequest.themeId());
        Reservation reservation = new Reservation(member.getName(), reservationRequest.date(), time, theme);

        try {
            return toResponse(reservationDao.save(reservation));
        } catch (DuplicateKeyException exception) {
            throw new IllegalArgumentException(DUPLICATE_RESERVATION_MESSAGE, exception);
        }
    }

    private void validateDuplicateReservation(ReservationRequest reservationRequest) {
        if (reservationDao.existsBySchedule(
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
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private Member findReservationMember(ReservationRequest reservationRequest, LoginMember loginMember) {
        if (!loginMember.isAdmin() || reservationRequest.name() == null || reservationRequest.name().isBlank()) {
            return memberService.findById(loginMember.id());
        }
        return memberService.findByName(reservationRequest.name());
    }

    private Time findTime(Long timeId) {
        try {
            return timeDao.findById(timeId);
        } catch (EmptyResultDataAccessException exception) {
            throw new IllegalArgumentException("예약 시간을 찾을 수 없습니다.", exception);
        }
    }

    private Theme findTheme(Long themeId) {
        try {
            return themeDao.findById(themeId);
        } catch (EmptyResultDataAccessException exception) {
            throw new IllegalArgumentException("예약 테마를 찾을 수 없습니다.", exception);
        }
    }

    private ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue()
        );
    }
}
