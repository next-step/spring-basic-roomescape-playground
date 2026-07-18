package roomescape.member;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberDao) {
        this.memberRepository = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member login(String email, String password) {
        return memberRepository.findByEmailAndPassword(email, password);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id);
    }

    public Member findByName(String name) {
        return memberRepository.findByName(name);
    }
}
