package roomescape.auth.dto;

import lombok.Getter;

@Getter
public class LoginMember { // 로그인한 사용자 정보를 담는 DTO

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
