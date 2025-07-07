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
            Member findMember = memberRepository.getByEmail(email);
            if (!findMember.getPassword().equals(password)) {
                throw new RoomEscapeException(ErrorCode.INVALID_LOGIN,"비밀번호가 틀렸습니다.");
            }
            return findMember;
        } catch (EmptyResultDataAccessException e) {
            throw new RoomEscapeException(ErrorCode.INVALID_LOGIN,"이메일이 잘못되었습니다.");
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
