package roomescape.auth.dto.response;


public record LoginMember(
	Long id,
	String name,
	String email,
	String role
) {
}
