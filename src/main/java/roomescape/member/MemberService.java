package roomescape.member;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    public static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(
                new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String authenticateAndGetToken(LoginRequest loginRequest) {
        MemberResponse memberResponse = findByEmailAndPassword(loginRequest.email(), loginRequest.password());
        return createToken(memberResponse);
    }

    private MemberResponse findByEmailAndPassword(String email, String password) {
        Member member = memberDao.findByEmailAndPassword(email, password);
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    private String createToken(MemberResponse memberResponse) {
        return Jwts.builder()
                .setSubject(memberResponse.getId().toString())
                .claim("name", memberResponse.getName())
                .claim("email", memberResponse.getEmail())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }
}
