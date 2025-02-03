package roomescape.auth;

public record LoginMember(
        String email,
        String name,
        Role role
) {
}
