package roomescape.Login;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import roomescape.jwt.JwtUtils;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.member.MemberResponse;

import java.util.Optional;

@Service
public class LoginService {
    private final MemberRepository memberRepository;
    private final JwtUtils jwtUtils;

    public LoginService(MemberRepository memberRepository, JwtUtils jwtUtils) {
        this.memberRepository = memberRepository;
        this.jwtUtils = jwtUtils;
    }

    public String login(LoginRequest loginRequest) {
        try {
            Optional<Member> member = memberRepository.findByEmailAndPassword(loginRequest.email(), loginRequest.password());
            return jwtUtils.createToken(member.get());
        } catch (DataAccessException e){
            throw new IllegalArgumentException("로그인 정보가 일치하지 않습니다.");
        }
    }

    public MemberResponse checkLogin(String token) {
        Long userId = jwtUtils.decodeToken(token);
        try {
            Optional<Member> member = memberRepository.findById(userId);
            return new MemberResponse(member.get().getId(), member.get().getName(), member.get().getEmail());
        } catch (DataAccessException exception){
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
    }

    public Member findByName(String name) {
        return memberRepository.findByName(name).stream().findFirst().get();
    }
}
