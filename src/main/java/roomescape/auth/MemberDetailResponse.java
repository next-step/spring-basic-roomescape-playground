package roomescape.auth;

public record MemberDetailResponse(
        Long id,
        String name,
        String email,
        String role
) {}
