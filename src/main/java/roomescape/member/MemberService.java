package roomescape.member;

import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public ViewMemberResponse findMemberByEmailAndPassword(String email, String password) {
        Member member = memberRepository.findByEmailAndPassword(email, password);
        if (member != null) {
            return new ViewMemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
        }
        return null;
    }

    public ViewMemberResponse findMemberById(Long id) {
        Member member = memberRepository.findById(id).orElse(null);
        if (member != null) {
            return new ViewMemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
        }
        return null;
    }

    public ViewMemberResponse findMemberByName(String name) {
        Member member = memberRepository.findByName(name);
        if (member != null) {
            return new ViewMemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
        }
        return null;
    }
}
