package roomescape.auth;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginResponse;
import roomescape.member.Member;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse createToken(Member member) {
        String accessToken = jwtTokenProvider.createToken(
                member.getId(),
                member.getName(),
                member.getRole());

        return new LoginResponse(accessToken);
    }

    public Long getMemberId(String token) {

        return jwtTokenProvider.findMemberId(token);
    }
}
