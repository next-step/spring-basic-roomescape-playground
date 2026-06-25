package roomescape.member;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;

import java.security.Key;

@Service
public class MemberService {
    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(
                new Member(
                        memberRequest.getName(),
                        memberRequest.getEmail(),
                        memberRequest.getPassword(),
                        "USER"
                )
        );

        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String login(LoginRequest loginRequest) {
        Member member = memberDao.findByEmailAndPassword(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        return createToken(member);
    }

    public LoginCheckResponse checkLogin(LoginMember loginMember) {
        return new LoginCheckResponse(loginMember.getName());
    }

    public LoginMember findLoginMemberByToken(String token) {
        Long memberId = extractMemberId(token);
        Member member = memberDao.findById(memberId);

        return new LoginMember(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getRole()
        );
    }

    private String createToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(getSecretKey())
                .compact();
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    private Long extractMemberId(String token) {
        String subject = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return Long.valueOf(subject);
    }
}
