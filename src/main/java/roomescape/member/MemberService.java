package roomescape.member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final String secretKey;

    private MemberDao memberDao;

    public MemberService(
            @Value("${roomescape.auth.jwt.secret}")
            String secretKey,
            MemberDao memberDao) {
        this.secretKey = secretKey;
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
        Member member = getMemberByEmailAndPassword(email, password);
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    private Member getMemberByEmailAndPassword(String email, String password) {
        return memberDao.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }

    private String createToken(MemberResponse memberResponse) {
        return Jwts.builder()
                .setSubject(memberResponse.getId().toString())
                .claim("name", memberResponse.getName())
                .claim("email", memberResponse.getEmail())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public AuthUserNameResponse findByToken(String token) {
        Long memberId = parseMemberIdFromToken(token);

        Member member = getMemberById(memberId);

        return new AuthUserNameResponse(member.getName());
    }

    private Long parseMemberIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.valueOf(claims.getSubject());
    }

    private Member getMemberById(Long memberId) {
        return memberDao.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }

}
