package roomescape.reservation;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;
import roomescape.exception.*;
import roomescape.member.MemberDao;
import roomescape.theme.Theme;
import roomescape.theme.ThemeDao;
import roomescape.time.Time;
import roomescape.time.TimeDao;

import java.util.List;

@Service
public class ReservationService {
    private ReservationDao reservationDao;
    private MemberDao memberDao;
    private final TimeDao timeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao, MemberDao memberDao, TimeDao timeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
        this.timeDao = timeDao;
        this.themeDao = themeDao;
    }

    public ReservationResponse save(LoginMember loginMember, ReservationRequest reservationRequest) {
        String reservationName = loginMember.name();
        String requestedName = reservationRequest.getName();

        if (requestedName != null) {
            if (requestedName.isBlank()) {
                throw new InvalidReservationException("예약자 이름을 올바르게 입력해야 합니다.");
            }

            try {
                reservationName = memberDao.findByName(requestedName).getName();
            } catch (EmptyResultDataAccessException exception) {
                throw new NotFoundMemberException("예약할 회원을 찾을 수 없습니다.");
            }
        }

        Time time = timeDao.findById(reservationRequest.getTime())
                .orElseThrow(() -> new NotFoundTimeException("예약 시간을 찾을 수 없습니다."));

        Theme theme = themeDao.findById(reservationRequest.getTheme())
                .orElseThrow(() ->
                        new NotFoundThemeException("예약 테마를 찾을 수 없습니다.")
                );

        Reservation newReservation = Reservation.create(
                reservationName,
                reservationRequest.getDate(),
                time,
                theme
        );

        Reservation savedReservation;

        try {
            savedReservation = reservationDao.save(newReservation);
        } catch (DuplicateKeyException exception) {
            throw new DuplicateReservationException("이미 해당 날짜와 시간에 예약된 테마입니다.");
        }

        return new ReservationResponse(savedReservation.getId(), savedReservation.getName(), savedReservation.getTheme().getName(), savedReservation.getDate(), savedReservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        int deletedCount = reservationDao.deleteById(id);

        if (deletedCount == 0) {
            throw new NotFoundReservationException("삭제할 예약을 찾을 수 없습니다.");
        }
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
