package roomescape.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ErrorMessage;
import roomescape.exception.NotFoundDataException;

@Slf4j
@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(
                new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER")
        );
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member login(String email, String password) {
        Member member = memberRepository.findByEmailAndPassword(email, password)
                                        .orElseThrow(() -> new NotFoundDataException(ErrorMessage.INVALID_LOGIN_CREDENTIALS.getMessage()));
        log.info("로그인 성공: memberId={}, email={}", member.getId(), member.getEmail());
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
