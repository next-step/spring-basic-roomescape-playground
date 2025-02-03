package roomescape.auth;

import io.jsonwebtoken.Claims;

public record LoginMember(
        String email,
        String name,
        Role role
) {
    public static LoginMember from(Claims claims) {
        return new LoginMember(
                claims.getSubject(),
                claims.get("name", String.class),
                Role.valueOf(claims.get("role", String.class)));
    }
}
