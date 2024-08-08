package roomescape.member.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.member.controller.dto.MemberRequest;
import roomescape.member.controller.dto.MemberResponse;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;

@Service
public class MemberService {
    @Value("${roomescape.auth.jwt.secret}")
    private String secretKey;

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(requestToMember(memberRequest));
        return memberToResponse(member);
    }

    private Member requestToMember(MemberRequest memberRequest) {
        return new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(), "USER");
    }

    private MemberResponse memberToResponse(Member member) {
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String login(MemberRequest memberRequest) {
        Member member = memberRepository.findByEmailAndPassword(memberRequest.email(), memberRequest.password())
                .orElseThrow(() -> new RuntimeException("로그인 실패"));

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public MemberResponse getMemberByName(String memberName) {
        if (memberName == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        return memberRepository.findByName(memberName)
                .map(this::memberToResponse)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }
}
