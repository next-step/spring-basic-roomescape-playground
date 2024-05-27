package roomescape.waiting;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberService;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
public class WaitingService {
    @Autowired
    private MemberService memberService;
    @Autowired
    private WaitingRepository waitingRepository;
    @Autowired
    private TimeRepository timeRepository;
    @Autowired
    private ThemeRepository themeRepository;


    public WaitingResponse save(WaitingRequest waitingRequest, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token=memberService.extractTokenFromCookie(cookies); // 쿠키에서 토큰 추출
        Member member = memberService.findByToken(token); // 추출한 토큰으로 멤버 찾기
        Long memberId = member.getId(); // 내 아이디

        List<Waiting> waitingList = waitingRepository.findByThemeIdAndTimeIdAndDate(waitingRequest.getTheme(),waitingRequest.getTime(), waitingRequest.getDate());
        for(Waiting waiting:waitingList){
            if (waiting.getMemberId()==memberId){
                throw null;
            }
        }
        Time time = timeRepository.findById(waitingRequest.getTime()).orElseThrow(null);
        Theme theme = themeRepository.findById(waitingRequest.getTheme()).orElseThrow(null);



        Waiting waiting = new Waiting(memberId,theme,time,waitingRequest.getDate());
        Waiting savedWaiting = waitingRepository.save(waiting);


        return new WaitingResponse(savedWaiting.getId(),savedWaiting.getTheme(),savedWaiting.getTime(),savedWaiting.getDate());
    }
}
