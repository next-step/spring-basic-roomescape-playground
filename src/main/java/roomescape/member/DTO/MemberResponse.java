package roomescape.member.DTO;

import roomescape.member.Role;

public class MemberResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;

    public MemberResponse(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // role이 필요한 경우 대비
    // role은 일단 인터셉터에서 써야해서 추가함.
    public MemberResponse(Long id, String name, String email, Role role) {
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

    public Role getRole() {
        return role;
    }
}
