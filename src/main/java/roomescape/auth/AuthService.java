package roomescape.auth;

import org.springframework.stereotype.Service;
import roomescape.member.Member;

@Service
public class AuthService {

    private JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse createToken(Member member) {
        String accessToken = jwtTokenProvider.createToken(
                member.getName(),
                member.getEmail(),
                member.getPassword());

        return new LoginResponse(accessToken);
    }
}
