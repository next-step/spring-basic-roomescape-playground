package roomescape.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.AuthTokenService;
import roomescape.auth.LoginTokens;
import roomescape.exception.AuthenticationException;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberDao memberDao;
    private final AuthTokenService authTokenService;

    public MemberService(MemberDao memberDao, AuthTokenService authTokenService) {
        this.memberDao = memberDao;
        this.authTokenService = authTokenService;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), MemberRole.USER));
        return new MemberResponse(member.id(), member.name(), member.email());
    }

    public LoginTokens login(MemberRequest memberRequest) {
        Member member = memberDao.findByEmailAndPassword(memberRequest.getEmail(), memberRequest.getPassword())
                .orElseThrow(AuthenticationException::new);

        return authTokenService.createLoginTokens(member);
    }
}
