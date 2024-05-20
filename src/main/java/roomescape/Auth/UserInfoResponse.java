package roomescape.Auth;

public class UserInfoResponse {
    private String name;
    public UserInfoResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserInfoResponse{" +
                "name='" + name + '\'' +
                '}';
    }
}
