package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;

@Service
public class LoginService {
    private MemberRepository memberRepository;

    public LoginService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String findMemberName(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 id를 가진 멤버가 존재하지 않습니다."));
        return member.getName();
    }
}
