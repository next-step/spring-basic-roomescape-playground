package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.member.MemberResponse;

@Service
public class AuthService {
    private MemberDao memberDao;
    private TokenProvider tokenProvider;
    private Extractor extractor;

    public AuthService(MemberDao memberDao, TokenProvider tokenProvider, Extractor extractor) {
        this.memberDao = memberDao;
        this.tokenProvider = tokenProvider;
        this.extractor = extractor;
    }

    public String loginByEmailAndPassword(LoginRequest request) {
        try {
            Member member = memberDao.findByEmailAndPassword(request.email(), request.password());
            return tokenProvider.createToken(member);
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("로그인 정보가 불일치 합니다.");
        }
    }

    public MemberResponse checkLogin(HttpServletRequest request) {

        String token = extractToken(request);
        Long memberId = extractId(token);

        Member member = memberDao.findById(memberId);
        return MemberResponse.from(member);
    }

    private Long extractId(String token) {
        return extractor.extractId(token);
    }

    private String extractToken(HttpServletRequest request) {
        return extractor.extractToken(request);
    }

}
