package roomescape.auth;

import io.jsonwebtoken.Claims;
import jwt.JwtUtils;
import org.springframework.stereotype.Service;
import roomescape.exception.AuthenticationException;
import roomescape.member.Member;
import roomescape.member.MemberRole;

import java.util.Map;

@Service
public class AuthTokenService {
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 1000L * 60 * 30;
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 7;

    private final JwtUtils jwtUtils;

    public AuthTokenService(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public LoginTokens createLoginTokens(Member member) {
        LoginMemberInfo loginMember = toLoginMemberInfo(member);
        return new LoginTokens(
                createToken(loginMember, TokenType.ACCESS, ACCESS_TOKEN_EXPIRATION_TIME),
                createToken(loginMember, TokenType.REFRESH, REFRESH_TOKEN_EXPIRATION_TIME)
        );
    }

    public String createAccessToken(Member member) {
        return createToken(toLoginMemberInfo(member), TokenType.ACCESS, ACCESS_TOKEN_EXPIRATION_TIME);
    }

    public LoginMemberInfo parseAccessToken(String token) {
        return parseMember(token, TokenType.ACCESS);
    }

    public LoginMemberInfo parseRefreshToken(String token) {
        return parseMember(token, TokenType.REFRESH);
    }

    private LoginMemberInfo toLoginMemberInfo(Member member) {
        return new LoginMemberInfo(member.id(), member.name(), member.email(), member.role());
    }

    private String createToken(LoginMemberInfo member, TokenType tokenType, long expirationTime) {
        Map<String, Object> claims = Map.of(
                "id", member.id(),
                "name", member.name(),
                "role", member.role().name(),
                "type", tokenType.name()
        );
        return jwtUtils.createToken(member.email(), claims, expirationTime);
    }

    private LoginMemberInfo parseMember(String token, TokenType expectedTokenType) {
        try {
            Claims claims = jwtUtils.parseToken(token);
            TokenType tokenType = TokenType.from(claims.get("type", String.class));
            if (tokenType != expectedTokenType) {
                throw new IllegalArgumentException("Invalid token type");
            }
            return new LoginMemberInfo(
                    claims.get("id", Number.class).longValue(),
                    claims.get("name", String.class),
                    claims.getSubject(),
                    MemberRole.from(claims.get("role", String.class))
            );
        } catch (Exception exception) {
            throw new AuthenticationException();
        }
    }
}
