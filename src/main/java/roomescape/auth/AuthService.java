package roomescape.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.auth.client.jwt.JwtProperties;
import roomescape.auth.client.jwt.JwtProvider;
import roomescape.global.exception.RoomescapeUnauthorizedException;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public AuthService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    public String generateAccessToken(LoginRequest loginRequest) {
        String email = loginRequest.email();
        String password = loginRequest.password();
        Member findMember = memberRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new RoomescapeUnauthorizedException("회원 정보를 찾을 수 없습니다."));

        return jwtProvider.generateToken(findMember.getId(), findMember.getName(), findMember.getRole());
    }

    public LoginCheckResponse checkAccessToken(String accessToken) {
        String name = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(JwtProperties.SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .get("name", String.class);
        try {
            memberRepository.findByName(name);
            return new LoginCheckResponse(name);
        } catch (IncorrectResultSizeDataAccessException exception) {
            throw new RoomescapeUnauthorizedException("회원 정보를 찾을 수 없습니다." + name);
        }
    }
}
