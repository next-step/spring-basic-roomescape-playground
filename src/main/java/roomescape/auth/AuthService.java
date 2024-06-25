package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.member.MemberResponse;

@Service
public class AuthService {
    private MemberDao memberDao;
    private TokenProvider tokenProvider;
    private AuthrizationExctractor tokenExtractor;

    public AuthService(MemberDao memberDao, TokenProvider tokenProvider, AuthrizationExctractor tokenExtractor) {
        this.memberDao = memberDao;
        this.tokenProvider = tokenProvider;
        this.tokenExtractor = tokenExtractor;
    }

    public String createToken(LoginRequest request) {
        Member member = memberDao.findByEmailAndPassword(request.email(), request.password());
        return tokenProvider.createToken(member);
    }

    public String extractToken(HttpServletRequest request) {
        return tokenExtractor.extractToken(request);
    }

    public Long extractIdByToken(String token) {
        return tokenExtractor.extractIdByToken(token);
    }

    public MemberResponse findById(Long id) {
        Member member = memberDao.findById(id);
        return MemberResponse.from(member);
    }

}
