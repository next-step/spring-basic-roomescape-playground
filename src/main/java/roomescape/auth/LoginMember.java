package roomescape.auth;

//2단계
public record LoginMember(
        Long id,
        String name,
        String email,
        String role
) {
}
