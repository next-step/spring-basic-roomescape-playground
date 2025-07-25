package roomescape.member;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
    private static final String DEFAULT_ROLE = "USER";

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Transactional
    public MemberResponse create(MemberRequest memberRequest) {
        Member member = memberDao.save(
                new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(), DEFAULT_ROLE));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    @Transactional
    public Optional<Member> findByName(String name) {
        return memberDao.findByName(name);
    }
}
