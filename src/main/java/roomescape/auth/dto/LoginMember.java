package roomescape.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginMember {

    private Long id;
    private String name;
    private String email;
    private String role;
}
