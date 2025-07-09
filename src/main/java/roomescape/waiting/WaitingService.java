package roomescape.waiting;

import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.LoginMember;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Service
@Transactional(readOnly = true)
public class WaitingService {

    private final WaitingRepository waitingRepo;
    private final MemberDao memberDao;
    private final EntityManager em;

    public WaitingService(WaitingRepository waitingRepo,
                          MemberDao memberDao,
                          EntityManager em) {
        this.waitingRepo = waitingRepo;
        this.memberDao   = memberDao;
        this.em          = em;
    }

    @Transactional
    public WaitingResponse createWaiting(WaitingRequest req, LoginMember loginMember) {
        Member member = memberDao.findById(loginMember.id());
        Theme theme = em.getReference(Theme.class, req.getTheme());
        Time time = em.getReference(Time.class, req.getTime());

        Waiting w = new Waiting(req.getDate(), member, theme, time);
        waitingRepo.save(w);

        List<WaitingRank> ranks = waitingRepo.findWaitingRankByMemberId(loginMember.id());
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
        waitingRepo.deleteById(waitingId);
    }
}
