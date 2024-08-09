package roomescape.member;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberDao memberDao;
    private final String secretKey;

    public MemberService(MemberDao memberDao, @Value("${roomescape.auth.jwt.secret}") String secretKey) {
        this.memberDao = memberDao;
        this.secretKey = secretKey;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(
                new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    public TokenResDto createToken(TokenReqDto tokenReqDto) {
        Member member = memberDao.findByEmailAndPassword(tokenReqDto.email(), tokenReqDto.password());
        if (member == null) {
            throw new IllegalArgumentException("해당 유저의 정보를 확인할 수 없습니다");
        }
        String accessToken = Jwts.builder() // 공부
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
        return new TokenResDto(accessToken);
    }

    String extractTokenFromCookie(Cookie[] cookies) {   // Cookie에서 토큰 정보 추출
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }

    public MemberResponse getMemberFromToken(String token) {
        String memberId = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        Member member = memberDao.findById(Long.parseLong(memberId));
        if (member == null) {
            throw new IllegalArgumentException("찾을 수 없습니다.");
        }
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
