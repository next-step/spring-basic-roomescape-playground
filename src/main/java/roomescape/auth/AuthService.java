package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
public class AuthService {
    private final JwtTokenProvider tokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider tokenProvider, MemberDao memberDao) {
        this.tokenProvider = tokenProvider;
        this.memberDao = memberDao;
    }

    @Transactional
    public String createToken(LoginRequest loginRequest) {
        Member member = findMember(loginRequest);
        return tokenProvider.generateToken(member.getId(), member.getName(), member.getRole());
    }

    private Member findMember(LoginRequest loginRequest) {
        return memberDao.findByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다."));
    }

    @Transactional
    public Member findMemberByToken(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        validateTokenExpire(token);
        Long memberId = tokenProvider.extractIdFromToken(token);
        return memberDao.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("토큰의 사용자 정보가 유효하지 않습니다."));
    }

    private void validateTokenExpire(String token) {
        if (!tokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }
}
