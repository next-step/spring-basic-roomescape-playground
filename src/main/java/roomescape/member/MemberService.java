package roomescape.member;


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
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String createToken(String email, String password) {
        try {
            Member member = memberDao.findByEmailAndPassword(email, password);

            return Jwts.builder()
                    .setSubject(member.getId().toString())
                    .claim("name", member.getName())
                    .claim("role", member.getRole())
                    .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .compact();
        } catch (Exception e) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
    }

    public MemberResponse findMemberByToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
        }

        try {
            String name = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("name", String.class);

            Member member = memberDao.findByName(name);

            return new MemberResponse(member.getId(), member.getName(), member.getEmail());
        } catch (Exception e) {
            throw new IllegalArgumentException("유효하지 않은 토큰이거나 인증에 실패했습니다.");
        }
    }
}
