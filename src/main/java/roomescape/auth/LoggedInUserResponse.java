package roomescape.auth;

//현재 로그인된 사용자의 이름만을 반환
public class LoggedInUserResponse {
    private String name;

    public LoggedInUserResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
