package roomescape.reservation;

import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.theme.Theme;
import roomescape.time.Time;
import roomescape.waiting.Waiting;
import roomescape.waiting.WaitingDao;
import roomescape.waiting.WaitingRank;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationDao reservationDao;
    private final MemberDao memberDao;
    private final WaitingDao waitingDao;
    private final EntityManager em;

    public ReservationService(ReservationDao reservationDao,
                              WaitingDao waitingDao,
                              MemberDao memberDao, EntityManager em) {
        this.reservationDao = reservationDao;
        this.waitingDao = waitingDao;
        this.memberDao = memberDao;
        this.em = em;
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(r -> new ReservationResponse(
                        r.getId(),
                        r.getName(),
                        r.getTheme().getName(),
                        r.getDate(),
                        r.getTime().getValue()))
                .collect(Collectors.toList());
    }

    public List<MyReservationResponse> findMine(LoginMember loginMember) {
        Long memberId = loginMember.id();

        List<MyReservationResponse> reservations = reservationDao.findByMemberId(memberId).stream()
                .map(r -> new MyReservationResponse(
                        r.getId(), r.getTheme().getName(), r.getDate(), r.getTime().getValue()))
                .toList();

        List<WaitingRank> waitingRanks = waitingDao.findWaitingRankByMemberId(memberId);
        List<MyReservationResponse> waitings = waitingRanks.stream()
                .map(wr -> {
                    Waiting w = wr.waiting();
                    long rank = wr.rank() + 1;
                    String status = rank + "번째 예약대기";
                    return new MyReservationResponse(
                            w.getId(), w.getTheme().getName(), w.getDate(), w.getTime().getValue(), status);
                })
                .toList();

        List<MyReservationResponse> all = new ArrayList<>();
        all.addAll(reservations);
        all.addAll(waitings);
        return all;
    }

    //사용자 예약 시
    @Transactional
    public ReservationResponse saveUser(ReservationRequest req, LoginMember loginMember) {
        Member member = memberDao.findById(loginMember.id());
        Theme theme = em.getReference(Theme.class, req.getTheme());
        Time time = em.getReference(Time.class, req.getTime());
        Reservation r = new Reservation(req.getDate(), member, theme, time);
        em.persist(r);
        return new ReservationResponse(
                r.getId(), member.getName(), theme.getName(), r.getDate(), time.getValue()
        );
    }

    //관리자가 예약 시
    @Transactional
    public ReservationResponse saveAdmin(ReservationRequest req) {
        Theme theme = em.getReference(Theme.class, req.getTheme());
        Time time = em.getReference(Time.class, req.getTime());
        Reservation r = new Reservation(req.getDate(), req.getName(), theme, time);
        em.persist(r);
        return new ReservationResponse(
                r.getId(), r.getName(), theme.getName(), r.getDate(), time.getValue()
        );
    }

    @Transactional
    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }
}
