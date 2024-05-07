package roomescape.member;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginMember {
    private Long id;
    private String name;
    private String email;
    private String role;
}
