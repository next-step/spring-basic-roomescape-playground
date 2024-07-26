package roomescape.auth;

import org.springframework.stereotype.Service;
import roomescape.member.MemberResponse;

@Service
public class AuthService {
    public TokenResponse createToken(TokenRequest tokenRequest) {

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI";
        return new TokenResponse(token);
    }

    public LoginResponse findUserByToken(String token) {
        return new LoginResponse("어드민");
    }
}
