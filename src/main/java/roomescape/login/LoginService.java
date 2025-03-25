package roomescape.login;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.member.Member;

@Service
public class LoginService {

    }

    public String login(String email, String password) {
    }

    public LoginCheckResponse getUserInfoFromToken(String token) {

        String memberName = claims.get("name", String.class);
        return new LoginCheckResponse(member.getName());
    }
}
