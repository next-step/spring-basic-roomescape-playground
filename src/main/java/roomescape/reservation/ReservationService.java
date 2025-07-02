package roomescape.reservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Service
public class ReservationService {

    private ReservationDao reservationDao;
    private final MemberDao memberDao;

    @PersistenceContext
    private EntityManager em;

    public ReservationService(ReservationDao reservationDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.memberDao      = memberDao;
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
        return reservationDao.findByMemberId(memberId).stream()
                .map(r -> new MyReservationResponse(
                        r.getId(),
                        r.getTheme().getName(),
                        r.getDate(),
                        r.getTime().getValue()
                ))
                .toList();
    }

    //사용자 예약 시
    @Transactional
    public ReservationResponse saveUser(ReservationRequest req, LoginMember loginMember) {
        Member member = memberDao.findById(loginMember.id());
        Theme theme = em.getReference(Theme.class, req.getTheme());
        Time  time  = em.getReference(Time.class,  req.getTime());
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
        Time  time  = em.getReference(Time.class,  req.getTime());
        Reservation r = new Reservation(req.getDate(), req.getName(), theme, time);
        em.persist(r);
        return new ReservationResponse(
                r.getId(), r.getName(), theme.getName(), r.getDate(), time.getValue()
        );
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }
}
