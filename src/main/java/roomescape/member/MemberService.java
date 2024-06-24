package roomescape.member;

import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public ViewMemberResponse findMemberByEmailAndPassword(String email, String password) {
        Member member = memberDao.findByEmailAndPassword(email, password);
        if (member != null) {
            return new ViewMemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
        }
        return null;
    }

    public ViewMemberResponse findMemberById(Long id) {
        Member member = memberDao.findById(id);
        if (member != null) {
            return new ViewMemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
        }
        return null;
    }

    // New method to find member by name
    public ViewMemberResponse findMemberByName(String name) {
        Member member = memberDao.findByName(name);
        if (member != null) {
            return new ViewMemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
        }
        return null;
    }
}
