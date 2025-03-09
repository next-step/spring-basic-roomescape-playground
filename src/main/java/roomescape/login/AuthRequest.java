package roomescape.login;

public record AuthRequest(
        String email,
        String password
) {}
