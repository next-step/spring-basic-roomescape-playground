package roomescape.member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import roomescape.exception.InvalidRequestException;

@Entity
public class Member {

    private static final String EMAIL_REGEX = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String role;

    protected Member() {
    }

    public Member(Long id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public Member(String name, String email, String password, String role) {
        validateName(name);
        validateEmail(email);
        validatePasswordNotBlank(password);
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidRequestException("이름은 비어있을 수 없습니다.");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidRequestException("이메일은 비어있을 수 없습니다.");
        }
        if (!email.matches(EMAIL_REGEX)) {
            throw new InvalidRequestException("이메일 형식이 올바르지 않습니다.");
        }
    }

    private void validatePasswordNotBlank(String password) {
        if (password == null || password.isBlank()) {
            throw new InvalidRequestException("비밀번호는 비어있을 수 없습니다.");
        }
    }

    // 스스로 권한을 확인하도록 수정
    public boolean isAdmin() {
        return "ADMIN".equals(this.role);
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
}
