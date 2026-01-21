package roomescape.member;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ErrorMessage;
import roomescape.exception.NotFoundDataException;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest memberRequest) {
        String hashedPassword = BCrypt.hashpw(memberRequest.password(), BCrypt.gensalt());
        Member member = memberRepository.save(
                new Member(memberRequest.name(), memberRequest.email(), hashedPassword, "USER")
        );
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                                        .orElseThrow(() -> new NotFoundDataException(ErrorMessage.INVALID_LOGIN_CREDENTIALS.getMessage()));

        if (!BCrypt.checkpw(password, member.getPassword())) {
            throw new NotFoundDataException(ErrorMessage.INVALID_LOGIN_CREDENTIALS.getMessage());
        }
        return member;
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                               .orElseThrow(() -> new NotFoundDataException(ErrorMessage.MEMBER_NOT_FOUND_BY_ID.format(id)));
    }

    public Member findByName(String name) {
        return memberRepository.findByName(name)
                               .orElseThrow(() -> new NotFoundDataException(ErrorMessage.MEMBER_NOT_FOUND_BY_NAME.format(name)));
    }
}
