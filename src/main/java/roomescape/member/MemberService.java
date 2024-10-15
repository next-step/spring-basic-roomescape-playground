package roomescape.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import roomescape.global.auth.JwtUtils;
import roomescape.member.controller.dto.MemberLoginRequest;
import roomescape.member.controller.dto.MemberRequest;
import roomescape.member.controller.dto.MemberResponse;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtils jwtUtils;

    public MemberService(MemberRepository memberRepository, JwtUtils jwtUtils) {
        this.memberRepository = memberRepository;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(
            new Member(memberRequest.name(),
                memberRequest.email(),
                memberRequest.password(),
                "USER")
        );

        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String login(MemberLoginRequest request) {
        Member member = memberRepository.findByEmailAndPassword(
            request.email(),
            request.password()
        ).orElseThrow(() -> new IllegalArgumentException("계정정보가 올바르지 않습니다."));

        return jwtUtils.createToken(member);
    }

    public Member checkLogin(String token) {
        Long memberId = jwtUtils.getMemberId(token);

        return memberRepository.getById(memberId);
    }
}
