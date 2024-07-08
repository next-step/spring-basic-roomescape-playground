package roomescape.domain.member.dto;

public record MemberLoginRequest(
        String password,
        String email
) {
}
