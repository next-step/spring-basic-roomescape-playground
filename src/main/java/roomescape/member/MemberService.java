package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
//import roomescape.auth.AuthorizationExtractor;
import roomescape.Jwt.JwtUtils;

import java.util.Arrays;

@Service
public class MemberService {
    private MemberDao memberDao;
    private JwtUtils jwtUtils;

    public MemberService(MemberDao memberDao, JwtUtils jwtUtils) {
        this.memberDao = memberDao;
        this.jwtUtils = jwtUtils;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member getMemberFromCookie(HttpServletRequest httpServletRequest) {
        Cookie[] cookies  = httpServletRequest.getCookies();
        String token = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("token")).findFirst().map(Cookie::getValue).orElseThrow();
        Member member = memberDao.findById(jwtUtils.extractMemberId(token));
        return member;
    }
}
