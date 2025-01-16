package roomescape.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private static final String NAME_CLAIM = "name";
    private static final String EMAIL_CLAIM = "email";
    private static final String ROLE_CLAIM = "role";

    private final String secretKey;
    private Key key;

    public TokenService(@Value("${roomescape.auth.jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createToken(MemberTokenDto memberTokenDto) {
        return Jwts.builder()
                .setSubject(memberTokenDto.id().toString())
                .claim(NAME_CLAIM, memberTokenDto.name())
                .claim(EMAIL_CLAIM, memberTokenDto.email())
                .claim(ROLE_CLAIM, memberTokenDto.role())
                .signWith(key)
                .compact();
    }
}
