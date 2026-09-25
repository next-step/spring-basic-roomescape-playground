package roomescape.member;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import roomescape.config.PasswordConfig;
import roomescape.exception.AuthenticationException;
import roomescape.exception.DuplicateException;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    // 이메일 중복 검증 로직 추가
    public MemberResponse createMember(MemberRequest memberRequest) {
        if (memberRepository.existsByEmail(memberRequest.getEmail())) {
            throw new DuplicateException("이미 가입되어있는 이메일입니다.");
        }
        // 요청에서 받은 비밀번호를 암호화한 후 DB에 저장
        String encodedPassword = passwordEncoder.encode(memberRequest.getPassword());
        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(), encodedPassword, "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    // DB에 쿼리를 보내 검증하는 방식에서 회원을 이메일로 조회해서 가져온 후 비밀번호를 검증하도록 수정
    public String login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .filter(m -> passwordEncoder.matches(password, m.getPassword()))
                .orElseThrow(() -> new AuthenticationException("이메일 또는 비밀번호가 일치하지 않습니다."));
        return jwtTokenProvider.createToken(member);
    }

    public Member findMemberByToken(String token) {
        long id = jwtTokenProvider.getMemberId(token);
        return memberRepository.findById(id)
                .orElseThrow(() -> new AuthenticationException("존재하지 않는 회원입니다."));
    }
}
