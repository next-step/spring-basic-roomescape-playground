package roomescape.member;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.provider.CookieProvider;
import roomescape.provider.TokenProvider;

@Service
public class MemberService {

    private MemberDao memberDao;
    private TokenProvider tokenProvider;
    private CookieProvider cookieProvder;

    public MemberService(MemberDao memberDao, TokenProvider tokenProvider, CookieProvider cookieProvder) {
        this.memberDao = memberDao;
        this.tokenProvider = tokenProvider;
        this.cookieProvder = cookieProvder;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public void login(LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
        Member member = memberDao.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        if(member == null) throw new IllegalArgumentException();
        String token = tokenProvider.createAccessToken(member);
        Cookie cookie = cookieProvder.createCookie(token);
        httpServletResponse.addCookie(cookie);
    }

    public LoginCheckResponse loginCheck(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = cookieProvder.extractTokenFromCookie(cookies);
        String memberName = tokenProvider.getMemberNameFromToken(token);
        Member member = memberDao.findByName(memberName);
        return new LoginCheckResponse(member.getName());
    }
}
