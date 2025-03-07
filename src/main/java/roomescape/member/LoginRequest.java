package roomescape.member;

public record LoginRequest(
        String password,
        String email
) {
}
