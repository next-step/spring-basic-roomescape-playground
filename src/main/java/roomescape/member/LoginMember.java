package roomescape.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginMember {

    final private Long id;
    final private String name;
    final private String email;
    final private String role;
}
