package roomescape.member;

public class MemberResponse {
    private Long id;
    private String name;
    private String email;

    private MemberResponse(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public static MemberResponse from(LoginMember loginMember) {
        return new MemberResponse(loginMember.getId(), loginMember.getName(), loginMember.getEmail());
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
}
