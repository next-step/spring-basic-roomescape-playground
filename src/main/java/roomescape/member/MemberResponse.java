package roomescape.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResponse {
    private Long id;
    private String name;
    private String email;
    private String role;

    public MemberResponse(Long memberId) {
        this.id = id;
    }
}
