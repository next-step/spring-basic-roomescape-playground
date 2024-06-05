package roomescape.member;

public record MemberLoginRequest(
        String password,
        String email
) {
}
