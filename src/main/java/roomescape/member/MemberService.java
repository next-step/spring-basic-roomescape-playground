package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import roomescape.auth.Extractor;
import roomescape.auth.LoginRequest;
import roomescape.auth.TokenProvider;

import java.util.Arrays;

@Service
public class MemberService {
    private final MemberDao memberDao;
    private final TokenProvider tokenProvider;
    private final Extractor extractor;

    public MemberService(MemberDao memberDao, TokenProvider tokenProvider, Extractor extractor) {
        this.memberDao = memberDao;
        this.tokenProvider = tokenProvider;
        this.extractor = extractor;
    }

    public LoginMember createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new LoginMember(member.getId(), member.getName(), member.getEmail());
    }

    public String loginByEmailAndPassword(LoginRequest request) {
        try {
            Member member = memberDao.findByEmailAndPassword(request.email(), request.password());
            return tokenProvider.createToken(member);
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("로그인 정보가 불일치 합니다.");
        }
    }

    public LoginMember checkLogin(HttpServletRequest request) {
        Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요합니다."));

        String token = extractToken(request);

        Long memberId = extractId(token);

        Member member = memberDao.findById(memberId);
        return LoginMember.from(member);
    }

    private Long extractId(String token) {
        return extractor.extractId(token);
    }

    private String extractToken(HttpServletRequest request) {
        return extractor.extractToken(request);
    }
}
