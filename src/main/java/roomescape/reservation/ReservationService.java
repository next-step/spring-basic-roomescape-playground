package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.theme.Theme;
import roomescape.theme.ThemeDao;
import roomescape.time.Time;
import roomescape.time.TimeDao;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final MemberDao memberDao;
    private final ThemeDao themeDao;
    private final TimeDao timeDao;

    public ReservationService(ReservationDao reservationDao, MemberDao memberDao,
                              ThemeDao themeDao, TimeDao timeDao) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
        this.themeDao = themeDao;
        this.timeDao = timeDao;
    }

    public ReservationResponse create(ReservationRequest request, LoginMember loginMember) {
        Member member;
        if (request.getName() != null && !request.getName().isBlank()) {
            member = memberDao.findByName(request.getName())
                    .orElseThrow(() -> new IllegalArgumentException("예약자 이름에 해당하는 멤버가 없습니다."));
        } else {
            if (loginMember == null) {
                throw new IllegalArgumentException("로그인이 필요합니다.");
            }
            member = memberDao.findById(loginMember.getId())
                    .orElseThrow(() -> new IllegalArgumentException("로그인한 사용자 정보를 찾을 수 없습니다."));
        }

        Theme theme = themeDao.findById(request.getTheme())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        Time time = timeDao.findById(request.getTime())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));


        Reservation reservationToSave = new Reservation(
                member.getName(),
                request.getDate(),
                time,
                theme
        );

        Reservation savedReservation = reservationDao.save(reservationToSave);

        return new ReservationResponse(
                savedReservation.getId(),
                savedReservation.getName(), // 저장된 엔티티에서 이름 가져오기
                savedReservation.getTheme().getName(),
                savedReservation.getDate(),
                savedReservation.getTime().getValue()
        );
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(
                        it.getId(),
                        it.getName(), // Member 객체에서 이름 가져오기
                        it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getValue()
                ))
                .toList();
    }
}
