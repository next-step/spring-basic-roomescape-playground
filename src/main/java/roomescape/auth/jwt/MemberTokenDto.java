package roomescape.auth.jwt;

public record MemberTokenDto (
	Long id,
	String name,
	String role
) {
}
