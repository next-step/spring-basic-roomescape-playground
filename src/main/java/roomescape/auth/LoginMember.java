package roomescape.auth;

import io.jsonwebtoken.Claims;

public record LoginMember(
        String email,
        String name,
        String role
) {
    public static LoginMember from(Claims claims) {
        return new LoginMember(
                claims.getSubject(),
                claims.get("name", String.class),
                claims.get("role", String.class));
    }
}
