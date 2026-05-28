package roomescape.member;

import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepositroy mamberRepository;

    public MemberService(MemberRepositroy mamberRepository) {
        this.mamberRepository = mamberRepository;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = mamberRepository.save(new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member memberLogin(MemberLoginRequest memberLoginRequest) {
        String email = memberLoginRequest.email();
        String password = memberLoginRequest.password();
        Member member = mamberRepository.findByEmailAndPassword(email, password);
        if (member == null) {
            throw new LoginFailedException("아이디 혹은 비밀번호를 확인해 주세요");
        }
        return member;
    }

    public Member findById(int id) {
        return mamberRepository.findById(id);
    }
}
