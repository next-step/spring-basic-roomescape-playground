package roomescape.auth.dto.response;

public record MemberTokenDto (
	Long id,
	String name,
	String role
) {
}
