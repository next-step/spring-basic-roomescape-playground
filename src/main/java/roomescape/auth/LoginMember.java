package roomescape.auth;

import java.util.Map;

public record LoginMember(
        String email,
        String name,
        String role
) {
    public static LoginMember fromClaims(Map<String, Object> claims) {
        String email = (String) claims.get("sub");
        String name = (String) claims.get("name");
        String role = (String) claims.get("role");
        return new LoginMember(email, name, role);
    }
}
