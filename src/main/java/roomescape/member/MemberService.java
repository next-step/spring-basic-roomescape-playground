package roomescape.member;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import roomescape.auth.MemberAuthContext;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(
                new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public MemberAuthContext loginByEmailAndPassword(MemberLoginRequest request) {
        try {
            Member member = memberRepository.findByEmailAndPassword(request.email(), request.password());
            return new MemberAuthContext(member.getName(), member.getRole());
        } catch (DataAccessException exception) {
            throw new IllegalArgumentException("로그인 정보가 불일치 합니다.");
        }
    }

    public MemberResponse checkLogin(MemberAuthContext authContext) {
        try {
            Member member = memberRepository.findByName(authContext.name());
            return new MemberResponse(member.getId(), member.getName(), member.getEmail());
        } catch (DataAccessException exception) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
    }
}
