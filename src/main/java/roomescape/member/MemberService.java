package roomescape.member;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;

@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    public Member authenticate(String email, String password) {
        try {
            return memberDao.findByEmailAndPassword(email, password);
        } catch (EmptyResultDataAccessException e) {
            throw new RoomEscapeException(ErrorCode.INVALID_LOGIN);
        }
    }

    public MemberResponse findById(Long memberId) {
        Member findMember = memberDao.findById(memberId);
        return new MemberResponse(findMember.getId(), findMember.getName(), findMember.getEmail(), findMember.getRole());
    }

}
