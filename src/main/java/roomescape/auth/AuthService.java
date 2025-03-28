package roomescape.auth;

import org.springframework.stereotype.Service;
import roomescape.global.exception.RoomescapeUnauthorizedException;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Service
public class AuthService {

    private final MemberRepository memberRepository;

    public AuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findMemberByEmailAndPassword(String email, String password) {
        return memberRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() ->
                        new RoomescapeUnauthorizedException("이메일 또는 패스워드가 잘못되었습니다."));
    }

    public Member findById(long id) {
        return memberRepository.findById(id)
                .orElseThrow(() ->
                        new RoomescapeUnauthorizedException("회원 정보를 찾을 수 없습니다."));
    }
}
