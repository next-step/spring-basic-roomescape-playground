package roomescape.auth;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.AuthRequest;
import roomescape.auth.dto.AuthResponse;
import roomescape.auth.dto.MemberDetailResponse;
import roomescape.auth.jwt.JwtTokenProvider;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
public class AuthService {
    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponse login(AuthRequest authRequest) {
        Member foundMember = memberDao.findByEmailAndPassword(authRequest.email(), authRequest.password());
        String accessToken = jwtTokenProvider.createToken(foundMember);
        return new AuthResponse(accessToken);
    }

    public MemberDetailResponse checkLogin(String token) {
        Long memberId = jwtTokenProvider.getMemberId(token);
        Member member = memberDao.findById(memberId);

        return new MemberDetailResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
