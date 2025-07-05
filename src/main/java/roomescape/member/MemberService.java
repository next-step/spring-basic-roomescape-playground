package roomescape.member;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;

@Service
@Transactional
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        validateDuplicateEmail(memberRequest);
        Member member = memberRepository.save(memberRequest.toEntity());
        return MemberResponse.from(member);
    }

    public Member authenticate(String email, String password) {
        try {
            return memberRepository.getByEmailAndPassword(email, password);
        } catch (EmptyResultDataAccessException e) {
            throw new RoomEscapeException(ErrorCode.INVALID_LOGIN);
        }
    }

    public MemberResponse getById(Long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() ->new RoomEscapeException(ErrorCode.MEMBER_NOT_FOUND));
        return MemberResponse.from(findMember);
    }

    private void validateDuplicateEmail(MemberRequest memberRequest) {
        if (memberRepository.findByEmail(memberRequest.email()).isPresent()) {
            throw new RoomEscapeException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

}
