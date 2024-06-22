package roomescape.member.dto;

public record MemberLoginRequest(
        String password,
        String email
) {
}
