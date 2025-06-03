package roomescape.member;

import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    public MemberResponse findById(Long id) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        Member member = optionalMember.orElseThrow(() -> new IllegalArgumentException("해당 id를 가진 Member 객체를 찾을 수 없습니다."));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    public MemberResponse findByEmailAndPassword(String email, String password) {
        Optional<Member> optionalMember = memberRepository.findByEmailAndPassword(email, password);
        Member member = optionalMember.orElseThrow(() -> new IllegalArgumentException("해당 email와 password를 가진 Member 객체를 찾을 수 없습니다."));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

}
