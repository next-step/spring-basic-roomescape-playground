package roomescape.member;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import roomescape.util.JwtUtil;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member findMemberByToken(String token) {
        Claims claims = jwtUtil.parseClaims(token);

        Double idDouble = claims.get("id", Double.class);
        Long id = null;
        if (idDouble != null) {
            id = idDouble.longValue();
        }

        return memberRepository.findById(id).orElse(null);
    }
}
