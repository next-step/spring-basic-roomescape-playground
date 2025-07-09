package roomescape.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginMember { // 로그인한 사용자 정보를 담는 DTO

    private Long id;
    private String name;
    private String email;
    private String role;
}
