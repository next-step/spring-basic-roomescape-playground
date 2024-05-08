package roomescape.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginResponse {
    private String name;

    public LoginResponse(String name) {
    }
}