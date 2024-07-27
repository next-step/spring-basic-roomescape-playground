package roomescape.global;

public class LoginMember {
    private Long id;
    private String name;
    private String email;
    private String role;

    public LoginMember(final Long id, final String name, final String email, final String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
