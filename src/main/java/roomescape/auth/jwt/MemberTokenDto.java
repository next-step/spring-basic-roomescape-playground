package roomescape.auth.jwt;

public record MemberTokenDto(
        long id,
        String name,
        String email,
        String role
) {
}
