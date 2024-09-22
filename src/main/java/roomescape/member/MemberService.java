package roomescape.member;

import org.springframework.stereotype.Service;

import roomescape.auth.JwtTokenProvider;
import roomescape.member.controller.dto.MemberLoginRequest;
import roomescape.member.controller.dto.MemberRequest;
import roomescape.member.controller.dto.MemberResponse;

@Service
public class MemberService {

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(
            new Member(memberRequest.getName(),
                memberRequest.getEmail(),
                memberRequest.getPassword(),
                "USER")
        );

        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String login(MemberLoginRequest request) {
        Member member = memberDao.findByEmailAndPassword(
            request.email(),
            request.password()
        );

        return jwtTokenProvider.createToken(member);
    }

    public MemberResponse checkLogin(String token) {
        Long memberId = jwtTokenProvider.getMemberId(token);

        Member member = memberDao.findById(memberId)
            .orElseThrow(IllegalArgumentException::new);

        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }
}
