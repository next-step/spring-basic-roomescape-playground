package roomescape.member;

import org.springframework.stereotype.Service;

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

    public MemberResponse findById(Long id) {
        Member member = memberDao.findById(id);
        if (member == null) {
            throw new IllegalArgumentException("해당 id를 가진 Member 객체를 찾을 수 없습니다.");
        }
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    public MemberResponse findByEmailAndPassword(String email, String password) {
        Member member = memberDao.findByEmailAndPassword(email, password);
        if (member == null) {
            throw new IllegalArgumentException("해당 email와 password를 가진 Member 객체를 찾을 수 없습니다.");
        }
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

}
