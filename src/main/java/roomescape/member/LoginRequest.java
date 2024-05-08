package roomescape.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest {

    private String password;
    private String email;

//    @AllArgsConsrtuctor가 대신 구현
//    public LoginRequest(String password, String email) {
//        this.password = password;
//        this.email = email;
//    }

//    @Getter가 대신 구현
//    public String getEmail() {
//        return email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
}