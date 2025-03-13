package roomescape.auth.dto;

public record AuthRequest(
        String email,
        String password
) {}
