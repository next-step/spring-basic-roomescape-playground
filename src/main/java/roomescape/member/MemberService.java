package roomescape.member;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.auth.TokenProvider;
import roomescape.auth.TokenRequest;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;

@Service
public class MemberService {
    private final MemberDao memberDao;
    private final TokenProvider tokenProvider;

    public MemberService(MemberDao memberDao, TokenProvider tokenProvider) {
        this.memberDao = memberDao;
        this.tokenProvider = tokenProvider;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(
                new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String login(TokenRequest tokenRequest) {
        Member member = memberDao.findByEmailAndPassword(tokenRequest.email(),tokenRequest.password());
        return tokenProvider.createToken(member);
    }

    public Member getLoginMemberInfo(Cookie[] cookies) {
        String token = extractTokenFromCookie(cookies);

        String name = tokenProvider.parseMemberName(token);

        return memberDao.findByName(name);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        return "";
    }

}
