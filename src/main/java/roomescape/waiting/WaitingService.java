package roomescape.waiting;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Service
@Transactional(readOnly = true)
public class WaitingService {

    private final WaitingDao waitingDao;
    private final MemberDao memberDao;
    private final EntityManager em;

    public WaitingService(WaitingDao waitingDao,
                          MemberDao memberDao,
                          EntityManager em) {
        this.waitingDao = waitingDao;
        this.memberDao = memberDao;
        this.em = em;
    }

    @Transactional
    public WaitingResponse createWaiting(WaitingRequest req, LoginMember loginMember) {
        Member member = memberDao.findById(loginMember.id());
        Theme theme = em.getReference(Theme.class, req.getTheme());
        Time time = em.getReference(Time.class, req.getTime());

        Waiting w = new Waiting(req.getDate(), member, theme, time);
        waitingDao.save(w);

        List<WaitingRank> ranks = waitingDao.findWaitingRankByMemberId(loginMember.id());
        long myRank = ranks.stream()
                .filter(r -> r.waiting().getId().equals(w.getId()))
                .mapToLong(WaitingRank::rank)
                .findFirst()
                .orElse(0L);

        String status = myRank + "번째 예약대기";
        return new WaitingResponse(w.getId(), theme.getName(), req.getDate(), time.getValue(), status, myRank+1);
    }

    @Transactional
    public void cancelWaiting(Long waitingId) {
        waitingDao.deleteById(waitingId);
    }
}
