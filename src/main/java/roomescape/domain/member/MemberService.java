package roomescape.domain.member;

import static roomescape.domain.exception.ExceptionCode.*;

import org.springframework.stereotype.Service;
import auth.JwtUtils;
import roomescape.domain.exception.LoginException;
import roomescape.domain.member.dto.MemberLoginRequest;
import roomescape.domain.member.dto.MemberRequest;
import roomescape.domain.member.dto.MemberResponse;
import roomescape.domain.member.entity.Member;
import roomescape.domain.member.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtils jwtUtils;

    public MemberService(final MemberRepository memberRepository,
                         final JwtUtils jwtUtils) {
        this.memberRepository = memberRepository;
        this.jwtUtils = jwtUtils;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(
                new Member(
                        memberRequest.getName(), memberRequest.getEmail(),
                        memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String findMember(MemberLoginRequest request) {
        final Member member = memberRepository.findByEmailAndPassword(request.email(),
                        request.password())
                .orElseThrow(() -> new LoginException(REQUEST_INVALID.getMessage()));
        return jwtUtils.createToken(member);
    }
}
