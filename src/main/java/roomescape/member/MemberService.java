package roomescape.member;

import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public ViewMemberResponse findMemberByEmailAndPassword (String email, String password) {
        Member member = memberRepository.findByEmailAndPassword(email, password);
        return new ViewMemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    public ViewMemberResponse findMemberById (Long id) {
        Member member = memberRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("찾을 수 없는 member id 입니다."));
        return new ViewMemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
