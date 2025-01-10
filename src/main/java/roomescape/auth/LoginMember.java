package roomescape.auth;

import lombok.Getter;

@Getter
public class LoginMember {

    private Long id;
    private String name;
    private String email;
    private String role;

    public LoginMember(Long id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
