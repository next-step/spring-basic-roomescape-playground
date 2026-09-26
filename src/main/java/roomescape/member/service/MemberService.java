package roomescape.member.service;

import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Service;
import roomescape.member.auth.JwtTokenProvider;
import roomescape.member.auth.RevokedTokenStore;
import roomescape.member.auth.TokenPayload;
import roomescape.member.domain.LoginMember;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RevokedTokenStore revokedTokenStore;

    public MemberService(MemberRepository memberRepository,
                         JwtTokenProvider jwtTokenProvider,
                         RevokedTokenStore revokedTokenStore) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.revokedTokenStore = revokedTokenStore;
    }

    public MemberResult createMember(String name, String email, String password) {
        Member member = memberRepository.save(new Member(
                name,
                email,
                password,
                "USER"
        ));
        return new MemberResult(member.getId(), member.getName(), member.getEmail());
    }

    public String login(String email, String password) {
        try {
            Member member = memberRepository.findByEmailAndPassword(email, password)
                    .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));
            return jwtTokenProvider.createToken(member.getId());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.", exception);
        }
    }

    public LoginMember findLoginMemberByToken(String token) {
        try {
            TokenPayload tokenPayload = jwtTokenProvider.parseToken(token);
            if (revokedTokenStore.isRevoked(tokenPayload.tokenId())) {
                throw new IllegalArgumentException("로그아웃된 로그인 토큰입니다.");
            }

            Member member = memberRepository.findById(tokenPayload.memberId())
                    .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
            return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
        } catch (JwtException | IllegalArgumentException exception) {
            throw new IllegalArgumentException("유효하지 않은 로그인 토큰입니다.", exception);
        }
    }

    public void logout(String token) {
        TokenPayload tokenPayload = jwtTokenProvider.parseToken(token);
        revokedTokenStore.revoke(tokenPayload.tokenId(), tokenPayload.expiresAt());
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }

}
