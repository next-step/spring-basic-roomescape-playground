package roomescape.member;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginRequest;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.dto.MemberAuthInfo;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;
import roomescape.member.repository.MemberRepository;

@Service
public class MemberService {

    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(
                new Member(memberRequest.name(), memberRequest.email(),
                        memberRequest.password(), Role.USER));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public MemberAuthInfo getMemberWithLoginRequest(LoginRequest loginRequest) {
        Optional<Member> memberOptional = memberRepository.findByEmailAndPassword(
                loginRequest.email(),
                loginRequest.password());
        Member member = memberOptional.orElseThrow();

        return new MemberAuthInfo(
                member.getId(),
                member.getName(),
                member.getRole().name()
        );

    }

    public Optional<Member> findByName(String name) {
        return memberRepository.findByName(name);
    }
}
