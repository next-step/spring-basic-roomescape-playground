package roomescape.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
public class AuthService {

    private static final String NAME_CLAIM = "name";
    private static final String EMAIL_CLAIM = "email";
    private static final String ROLE_CLAIM = "role";

    private final String secretKey;
    private final MemberDao memberDao;

    private Key key;

    public AuthService(@Value("${roomescape.auth.jwt.secret}") String secretKey,
                       MemberDao memberDao) {
        this.secretKey = secretKey;
        this.memberDao = memberDao;
    }

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String loginWithEmailAndPassword(String email, String password) {
        Member member = memberDao.findByEmailAndPassword(email, password);

        if(member == null) {
            throw new IllegalArgumentException("잘못된 이메일 또는 비밀번호입니다.");
        }

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim(NAME_CLAIM, member.getName())
                .claim(EMAIL_CLAIM, member.getEmail())
                .claim(ROLE_CLAIM, member.getRole())
                .signWith(key)
                .compact();

    }
}
