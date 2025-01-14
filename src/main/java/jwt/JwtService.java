package jwt;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Service
public class JwtService {
    private final JwtUtils jwtUtils;
    private final MemberRepository memberRepository;

    public JwtService(JwtUtils jwtUtils, MemberRepository memberRepository) {
        this.jwtUtils = jwtUtils;
        this.memberRepository = memberRepository;
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = jwtUtils.getClaimsFromToken(token);
        try {
            return Long.valueOf(claims.getSubject());
        } catch (Exception e) {
            throw new IllegalArgumentException("Claims에서 User ID를 추출할 수 없습니다.", e);
        }
    }

    public Member getMemberFromToken(String token) {
        Long userId = getUserIdFromToken(token);
        Member member = memberRepository.findById(userId).get();
        if (member == null) {
            throw new IllegalArgumentException("토큰으로부터 유저를 찾을 수 없습니다.");
        }
        return member;
    }
}
