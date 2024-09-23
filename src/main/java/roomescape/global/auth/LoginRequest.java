package roomescape.global.auth;

public record LoginRequest(
    String email,
    String password
) {
}
