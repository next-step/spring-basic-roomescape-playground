package roomescape.domain.member;

public class LoginMember {

    private Long id;
    private String name;
    private String email;
    private String role;

    public LoginMember(Long id, String name, String email, String role) {
        validateFields(id, name, email, role);
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

    private void validateFields(Long id, String name, String email, String role) {
        if (id == null) {
            throw new IllegalArgumentException("LoginMember를 생성하기 위해 ID는 필수 필드입니다.");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("LoginMember를 생성하기 위해 name은 필수 필드입니다.");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("LoginMember를 생성하기 위해 email은 필수 필드입니다.");
        }

        if (!email.contains("@") || !email.endsWith(".com")) {
            throw new IllegalArgumentException("LoginMember.email 필드의 정규식이 올바르지 않습니다.");
        }

        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("LoginMember를 생성하기 위해 role은 필수 필드입니다.");
        }

        if (!role.equals("USER") && !role.equals("ADMIN")) {
            throw new IllegalArgumentException("잘못된 권한입니다.");
        }

    }
}
