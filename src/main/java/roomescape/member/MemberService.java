package roomescape.member;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final String secretKey;
    private final MemberRepository memberRepository; // Dao ➡️ Repository 변경

    private static final long TOKEN_VALIDITY_IN_MILLISECONDS = 3600000;

    public MemberService(MemberRepository memberRepository, @Value("${jwt.secret}") String secretKey) {
        this.memberRepository = memberRepository;
        this.secretKey = secretKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String createToken(String email, String password) {
        // Optional 덕분에 비즈니스 예외 처리가 한 줄로 명확해집니다.
        Member member = memberRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다."));

        Date now = new Date();
        Date validity = new Date(now.getTime() + TOKEN_VALIDITY_IN_MILLISECONDS);

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public MemberResponse findMemberByToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
        }

        try {
            String name = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("name", String.class);

            Member member = memberRepository.findByName(name)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰이거나 인증에 실패했습니다."));

            return new MemberResponse(member.getId(), member.getName(), member.getEmail());
        } catch (Exception e) {
            throw new IllegalArgumentException("유효하지 않은 토큰이거나 인증에 실패했습니다.");
        }
    }
}
