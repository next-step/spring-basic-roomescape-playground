package roomescape.member;

import org.springframework.stereotype.Service;
import roomescape.auth.AuthToken;
import roomescape.auth.AuthTokenProvider;
import roomescape.auth.AuthorizedMember;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthTokenProvider authTokenProvider;

    public MemberService(MemberRepository memberRepository, AuthTokenProvider authTokenProvider) {
        this.memberRepository = memberRepository;
        this.authTokenProvider = authTokenProvider;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(),
                memberRequest.getPassword(), Member.Role.USER));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public AuthToken authorizeMember(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmailAndPassword(loginRequest.email(), loginRequest.password());
        AuthorizedMember authorizedMember = AuthorizedMember.from(member);
        return authTokenProvider.generateSessionToken(authorizedMember);
    }
}
