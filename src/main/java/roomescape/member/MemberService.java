package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import roomescape.util.CookieUtil;
import roomescape.util.JwtUtil;


@Service
public class MemberService {
    private MemberDao memberDao;
    private JwtUtil jwtUtil;

    private CookieUtil cookieUtil;


    public MemberService(MemberDao memberDao, JwtUtil jwtUtil, CookieUtil cookieUtil) {
        this.memberDao = memberDao;
        this.cookieUtil = cookieUtil;
        this.jwtUtil = jwtUtil;
    }

    public MemberResponse.MemberInfoResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));

        return new MemberResponse.MemberInfoResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String login(String email, String password) {
        Member member = memberDao.findByEmailAndPassword(email, password);

        return jwtUtil.createToken(member.getId(), member.getName(), member.getRole());
    }

    public MemberResponse.AuthorizationResponse check(HttpServletRequest request) {
        String token = cookieUtil.extractTokenFromCookie(request);

        return new MemberResponse.AuthorizationResponse(jwtUtil.getName(token), jwtUtil.getRole(token));
    }


}
