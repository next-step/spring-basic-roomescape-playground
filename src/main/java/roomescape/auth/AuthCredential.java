package roomescape.auth;

public record AuthCredential(
        Long id,
        String name,
        String role
) {}
