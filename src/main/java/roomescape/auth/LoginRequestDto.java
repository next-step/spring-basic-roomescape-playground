package roomescape.auth;

public record LoginRequestDto(
        String email,
        String password
) {}
