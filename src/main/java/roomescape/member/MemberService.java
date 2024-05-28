package roomescape.member;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import roomescape.auth.JwtUtils;
import roomescape.provider.CookieProvider;
import roomescape.provider.TokenProvider;

import java.util.Map;

@Service
public class MemberService {
    private MemberRepository memberRepository;
    private JwtUtils jwtUtils;

    public MemberService(MemberRepository memberRepository, JwtUtils jwtUtils) {
        this.memberRepository = memberRepository;
        this.jwtUtils = jwtUtils;
    }
    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(),member.getRole());
    }

    public String login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmailAndPassword(loginRequest.getEmail(),loginRequest.getPassword());

        return jwtUtils.createToken(member.getId().toString(), Map.of("name", member.getName(), "role", member.getRole()));
    }

    public MemberResponse checkLogin (String token) {
        String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

        Long memberId = Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());
        Member member = memberRepository.findById(memberId).orElse(new Member());

        return findById(member.getId());
    }

    public MemberResponse findById(Long id) {
        Member member = memberRepository.findById(id).orElse(new Member());
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    public Member findMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}