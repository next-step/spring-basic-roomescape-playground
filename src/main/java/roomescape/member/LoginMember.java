package roomescape.member;

public class LoginMember {
	Long id;
	String name;
	String email;
	String password;

	public LoginMember(Long id, String name, String email, String password) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
	}
}
