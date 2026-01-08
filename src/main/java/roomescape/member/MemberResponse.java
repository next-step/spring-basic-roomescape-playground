package roomescape.member;

import lombok.Getter;

@Getter
public class MemberResponse {
    private Long id;
    private String name;
    private String email;

    public MemberResponse(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
