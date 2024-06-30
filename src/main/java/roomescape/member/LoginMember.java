package roomescape.member;

public class LoginMember {
    private Long id;
    private String name;
    private String email;

    public LoginMember(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static LoginMember from(Member member) {
        return new LoginMember(member.getId(), member.getName(), member.getEmail());
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
