package roomescape.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
