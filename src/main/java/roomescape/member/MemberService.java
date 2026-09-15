package roomescape.member;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import roomescape.exception.DuplicateMemberException;

@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        try {
            Member member = memberDao.save(
                    new Member(
                            memberRequest.getName(),
                            memberRequest.getEmail(),
                            memberRequest.getPassword(),
                            Role.USER
                    )
            );

            return new MemberResponse(
                    member.getId(),
                    member.getName(),
                    member.getEmail()
            );
        } catch (DuplicateKeyException exception) {
            throw new DuplicateMemberException("이미 가입된 이메일입니다.");
        }
    }
}
