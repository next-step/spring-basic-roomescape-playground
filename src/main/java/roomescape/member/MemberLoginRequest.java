package roomescape.member;

public record MemberLoginRequest(
        String email,
        String password
) {
}
