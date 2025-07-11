package roomescape.waiting;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
@Transactional(readOnly = true)
public class WaitingService {

    private final WaitingRepository waitingRepo;
    private final MemberRepository memberRepo;
    private final ThemeRepository themeRepo;
    private final TimeRepository timeRepo;

    public WaitingService(
            WaitingRepository waitingRepo,
            MemberRepository  memberRepo,
            ThemeRepository   themeRepo,
            TimeRepository    timeRepo
    ) {
        this.waitingRepo = waitingRepo;
        this.memberRepo  = memberRepo;
        this.themeRepo   = themeRepo;
        this.timeRepo    = timeRepo;
    }

    @Transactional
    public WaitingResponse createWaiting(WaitingRequest req, LoginMember loginMember) {
        Member member = memberRepo.findByIdOrThrow(loginMember.id());

        Theme theme = themeRepo.getReferenceById(req.getTheme());
        Time  time  = timeRepo.getReferenceById(req.getTime());

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
