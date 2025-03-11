package roomescape.auth;

public record AuthRequest(
        String email,
        String password
) {}
