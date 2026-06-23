package roomescape.member;

import org.springframework.stereotype.Service;
import roomescape.auth.DTO.LoginRequest;
import roomescape.member.DTO.MemberRequest;
import roomescape.member.DTO.MemberResponse;

@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public MemberResponse findByEmailAndPassword(LoginRequest request) {
        Member member = memberDao.findByEmailAndPassword(request.getEmail(), request.getPassword());
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public MemberResponse findByEmail(String email) {
        Member member = memberDao.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원정볼르 찾지 못했어요."));

        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }
}
