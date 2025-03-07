package roomescape.member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
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
        // TODO null check 하고 예외 반환 로직 필요
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
    
    public CheckResponse findByToken(String token) {
        Long memberId = parseMemberIdFromToken(token);

        Member member = getMemberById(memberId);

        return new CheckResponse(member.getName());
    }

    private  Long parseMemberIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.valueOf(claims.getSubject());
    }

    private Member getMemberById(Long memberId) {
        return memberDao.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }
    
}
