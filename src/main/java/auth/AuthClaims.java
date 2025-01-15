package auth;

public record AuthClaims(
        Long id,
        String name,
        String role
) {
}
