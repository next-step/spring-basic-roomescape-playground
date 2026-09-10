package roomescape.domain.member;

public class Member {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;

    public Member(Long id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public Member(String name, String email, String password, String role) {
        validateFields(name, email, password, role);
        this.name = name;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    private void validateFields(String name, String email, String password, String role) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Member를 생성하기 위해 name은 필수 필드입니다.");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Member를 생성하기 위해 email은 필수 필드입니다.");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Member를 생성하기 위해 password는 필수 필드입니다.");
        }

        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Member를 생성하기 위해 role은 필수 필드입니다.");
        }

    }
}
