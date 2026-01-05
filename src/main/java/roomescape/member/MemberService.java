package roomescape.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
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

    public Member login(String email, String password) {
        Member member = memberRepository.findByEmailAndPasswordOrThrow(email, password);
        log.info("로그인 성공: memberId={}, email={}", member.getId(), member.getEmail());
        return member;
    }

    public Member findById(Long id) {
        return memberRepository.findByIdOrThrow(id);
    }

    public Member findByName(String name) {
        return memberRepository.findByNameOrThrow(name);
    }
}
