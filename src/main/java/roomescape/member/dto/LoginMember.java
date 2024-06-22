package roomescape.member.dto;

import roomescape.member.Role;

public class LoginMember {

    private Long id;
    private String name;
    private String email;
    private Role role;

    public LoginMember(Long id, String name, String email, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public Role getRole() {
        return this.role;
    }
}
