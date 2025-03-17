package roomescape.member.dto.request;

public class MemberRequest {

    private final String name;
    private final String email;
    private final String password;

    public MemberRequest(String name, String email, String password) {

        validateName(name);
        validateEmail(email);
        validatePassword(password);

        this.name = name;
        this.email = email;
        this.password = password;
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
