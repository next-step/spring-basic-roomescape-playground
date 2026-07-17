package roomescape.login;

import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.member.MemberResponse;

@Service
public class LoginService {
    private final MemberRepository memberRepository;

    public LoginService(MemberRepository memberDao) {
        this.memberRepository = memberDao;
    }

    public Member login(String email, String password) {
        Member member = memberRepository.findByEmailAndPassword(email, password)
                .orElseThrow();

        return member;
    }

    public MemberResponse checkLogin(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow();
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow();

        return member;
    }


}
