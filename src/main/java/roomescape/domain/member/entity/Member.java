package roomescape.domain.member.entity;

import jakarta.persistence.*;

@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String nickname;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 255)
    private String role;

    protected Member() {
    }

    public Member(String nickname, String email, String password, String role) {
        validateFields(nickname, email, password, role);
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
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

    private void validateFields(String nickname, String email, String password, String role) {

        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("Member를 생성하기 위해 name은 필수 필드입니다.");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Member를 생성하기 위해 email은 필수 필드입니다.");
        }

        if (!email.contains("@") || !email.endsWith(".com")) {
            throw new IllegalArgumentException("Member.email 필드의 정규식이 올바르지 않습니다.");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Member를 생성하기 위해 password는 필수 필드입니다.");
        }

        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Member를 생성하기 위해 role은 필수 필드입니다.");
        }

        if (!role.equals("USER") && !role.equals("ADMIN")) {
            throw new IllegalArgumentException("잘못된 권한입니다.");
        }

    }
}
