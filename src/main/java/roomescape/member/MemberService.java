package roomescape.member;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import roomescape.util.CookieUtil;
import roomescape.util.JwtProvider;


@Service
public class MemberService {
    private MemberDao memberDao;

    private JwtProvider jwtProvider;


    public MemberService(MemberDao memberDao, JwtProvider jwtProvider) {
        this.memberDao = memberDao;
        this.jwtProvider = jwtProvider;
    }

    public MemberResponse.MemberInfoResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));

        return new MemberResponse.MemberInfoResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String login(String email, String password) {
        Member member = memberDao.findByEmailAndPassword(email, password);

        return jwtProvider.createToken(member.getId(), member.getName(), member.getRole());
    }

    public MemberResponse.AuthorizationResponse check(HttpServletRequest request) {
        String token = CookieUtil.extractTokenFromCookie(request);

        return new MemberResponse.AuthorizationResponse(jwtProvider.getName(token), jwtProvider.getRole(token));
    }


}
