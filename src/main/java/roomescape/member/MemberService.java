package roomescape.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.dto.LoginRequest;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(
                new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER")
        );
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    @Transactional(readOnly = true)
    public Member login(LoginRequest request) { // 반환 타입을 String(토큰)에서 Member로 변경
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다."));

        if (!member.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        return member;
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다."));
    }

    public MemberResponse findMemberResponseById(Long id) {
        Member member = findById(id);
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }
}
