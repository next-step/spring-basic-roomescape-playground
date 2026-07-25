package roomescape.login;

import lombok.Getter;


@Getter
public class LoginMember {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final String role;

    public LoginMember(Long id, String name, String email, String password, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

}
