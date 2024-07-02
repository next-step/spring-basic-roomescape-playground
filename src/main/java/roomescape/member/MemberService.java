package roomescape.member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.instructure.JwtTokenProvider;

import java.util.Optional;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Value("${roomescape.auth.jwt.secret}")
    String secretKey;

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public MemberResponse findMember(String email, String password) {
        Member member = memberRepository.findByEmailAndPassword(email, password);
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public MemberResponse findMemberById(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String createToken(MemberResponse memberResponse) {
        String accessToken = jwtTokenProvider.createToken(memberResponse);
        return accessToken;
    }

    public void createCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public Member extractMemberFromToken(String token) {
        Long memberId = extractMemberIdFromToken(token);
        if (memberId != null) {
            Optional<Member> optionalMember = memberRepository.findById(memberId);
            return optionalMember.orElse(null);
        }
        return null;
    }

    public Long extractMemberIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.valueOf(claims.getSubject());
    }

    public String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}