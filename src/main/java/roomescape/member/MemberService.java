package roomescape.member;

import org.springframework.stereotype.Service;
import roomescape.error.ErrorCode;
import roomescape.util.JwtUtil;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

        if (!member.checkPassword(request.getPassword())) {
            throw new IllegalArgumentException(ErrorCode.PASSWORD_MISMATCH.getMessage());
        }

        return JwtUtil.createToken(member);
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), Role.USER));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));
    }
}
