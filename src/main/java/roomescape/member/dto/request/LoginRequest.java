package roomescape.member.dto.request;

public record LoginRequest(
        String password,
        String email
) {
}
