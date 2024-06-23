package roomescape.auth;

public record LoginMember(
	Long id,
	String name,
	String email,
	String password) {
}
