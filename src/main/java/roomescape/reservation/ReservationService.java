package roomescape.reservation;

import java.util.ArrayList;
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
import roomescape.waiting.WaitingRank;
import roomescape.waiting.WaitingRepository;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepo;
    private final WaitingRepository     waitingRepo;
    private final MemberRepository      memberRepo;
    private final ThemeRepository themeRepo;
    private final TimeRepository timeRepo;

    public ReservationService(
            ReservationRepository reservationRepo,
            WaitingRepository     waitingRepo,
            MemberRepository      memberRepo,
            ThemeRepository       themeRepo,
            TimeRepository        timeRepo
    ) {
        this.reservationRepo = reservationRepo;
        this.waitingRepo     = waitingRepo;
        this.memberRepo      = memberRepo;
        this.themeRepo       = themeRepo;
        this.timeRepo        = timeRepo;
    }

    public List<ReservationResponse> findAll() {
        return reservationRepo.findAllWithFetch().stream()
                .map(r -> new ReservationResponse(
                        r.getId(),
                        r.getName(),
                        r.getTheme().getName(),
                        r.getDate(),
                        r.getTime().getValue()
                ))
                .toList();
    }

    public List<MyReservationResponse> findMine(LoginMember loginMember) {
        Long memberId = loginMember.id();

        List<MyReservationResponse> confirmed = reservationRepo
                .findByMemberId(memberId)
                .stream()
                .map(r -> new MyReservationResponse(
                        r.getId(),
                        r.getTheme().getName(),
                        r.getDate(),
                        r.getTime().getValue()
                ))
                .toList();

        List<WaitingRank> waitingRanks = waitingRepo.findWaitingRankByMemberId(memberId);
        List<MyReservationResponse> waitings = waitingRanks.stream()
                .map(wr -> {
                    var w    = wr.waiting();
                    long rank = wr.rank() + 1;
                    return new MyReservationResponse(
                            w.getId(),
                            w.getTheme().getName(),
                            w.getDate(),
                            w.getTime().getValue(),
                            rank + "번째 예약대기"
                    );
                })
                .toList();

        var all = new ArrayList<MyReservationResponse>();
        all.addAll(confirmed);
        all.addAll(waitings);
        return all;
    }

    //사용자 예약 시
    @Transactional
    public ReservationResponse saveUser(ReservationRequest req, LoginMember loginMember) {
        Member member = memberRepo.findById(loginMember.id())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Theme theme = themeRepo.getReferenceById(req.getTheme());
        Time time   = timeRepo.getReferenceById(req.getTime());

        Reservation r = new Reservation(req.getDate(), member, theme, time);
        reservationRepo.save(r);

        return new ReservationResponse(
                r.getId(), member.getName(), theme.getName(), r.getDate(), time.getValue()
        );
    }

    //관리자가 예약 시
    @Transactional
    public ReservationResponse saveAdmin(ReservationRequest req) {
        Theme theme = themeRepo.getReferenceById(req.getTheme());
        Time time   = timeRepo.getReferenceById(req.getTime());

        Reservation r = new Reservation(req.getDate(), req.getName(), theme, time);
        reservationRepo.save(r);

        return new ReservationResponse(
                r.getId(), r.getName(), theme.getName(), r.getDate(), time.getValue()
        );
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepo.deleteById(id);
    }
}
