package roomescape.waiting;

import auth.JwtUtils;
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
    private JwtUtils jwtUtils;

    public WaitingService(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }



    public WaitingResponse save(WaitingRequest waitingRequest, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token=jwtUtils.extractTokenFromCookie(cookies); //토큰 추출
        Long memberId = Long.valueOf(jwtUtils.extractSubject(token));

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
