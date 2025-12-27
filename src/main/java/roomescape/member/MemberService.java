package roomescape.member;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.exception.NotFoundDataException;

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

    public Member findById(Long id) {
        try {
            return memberDao.findById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundDataException("ID " + id + "에 해당하는 회원이 존재하지 않습니다.");
        }
    }

    public Member findByName(String name) {
        try {
            return memberDao.findByName(name);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundDataException("이름이 '" + name + "'인 회원이 존재하지 않습니다.");
        }
    }
}
