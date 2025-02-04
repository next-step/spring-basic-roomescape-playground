package roomescape.auth;

public record LoginMember(
        String email,
        String name,
        Role role
) {
    public LoginMember(String email, String name, Role role) {
        validateEmail(email);
        validateName(name);
        validateRole(role);
        this.email = email;
        this.name = name;
        this.role = role;
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Invalid name");
        }
    }

    private void validateRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Invalid role");
        }
    }
}
