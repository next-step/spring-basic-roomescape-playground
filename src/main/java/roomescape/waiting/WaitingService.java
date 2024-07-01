package roomescape.waiting;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
public class WaitingService {
    @Autowired
    private WaitingRepository waitingRepository;
    @Autowired
    private TimeRepository timeRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private MemberRepository memberRepository;

    @PostMapping("/waitings")
    public WaitingResponse save(WaitingRequest request, Long memberId) {
        Theme theme = themeRepository.findById(request.getTheme()).orElseThrow();
        Time time = timeRepository.findById(request.getTime()).orElseThrow();
        Member member = memberRepository.findById(memberId).orElseThrow();

        Waiting waiting =
                waitingRepository.save(new Waiting(request.getDate(), time, theme, member));
//        int rank = waitingRepository.findWaitingsWithRankByMemberId(member.getId());

        return new WaitingResponse(waiting.getId(), waiting.getDate(), time.getValue(), theme.getName(), member.getId(), 1);
    }

//    @GetMapping("/waitings-mine")
//    public List<WaitingResponse> findWaitingList(WaitingRequest request, Long memberId) {
//        Theme theme = themeRepository.findById(request.getTheme()).orElseThrow();
//        Time time = timeRepository.findById(request.getTime()).orElseThrow();
//
//        return waitingRepository.findWaitingsWithRankByMemberId(request.getMemberId()).stream()
//                .map(it -> new WaitingResponse(
//                        it.getWaiting().getId(),
//                        it.getWaiting().getDate(),
//                        time.getValue(),
//                        theme.getName(),
//                        request.getMemberId(),
//                        it.getRank()))
//                .toList();
//    }

    public void deleteById(Long id) {
        waitingRepository.deleteById(id);
    }
}
