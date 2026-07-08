package roomescape.member;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.token.JwtTokenProvider;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createToken(String email, String password) {
        validateLogin(email, password);
        Member member = getMemberByEmailAndPassword(email, password);
        return jwtTokenProvider.createToken(member);
    }

    public Member extractMemberFromToken(String token) {
        String name = jwtTokenProvider.getTokenPayload(token).get("name", String.class);
        return getMemberByName(name);
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(
                new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(),
                        MemberRole.USER.toString()));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    private void validateLogin(String email, String password) {
        if (email.isBlank() || password.isBlank()) {
            throw new IllegalArgumentException("이메일과 패스워드를 모두 입력해주세요");
        }
    }

    private Member getMemberByName(String name) {
        try {
            return memberRepository.findByName(name);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("이름이 틀렸습니다");
        }
    }

    private Member getMemberByEmailAndPassword(String email, String password) {
        try {
            return memberRepository.findByEmailAndPassword(email, password);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("이메일 또는 패스워드가 틀렸습니다");
        }
    }
}
