package roomescape.member.controller.dto;

public record MemberLoginRequest(
    String email,
    String password
) {
}
