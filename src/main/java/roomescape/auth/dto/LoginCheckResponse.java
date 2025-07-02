package roomescape.auth.dto;

import lombok.Getter;

@Getter
public class LoginCheckResponse {

    private String name;

    public LoginCheckResponse(String name) {
        this.name = name;
    }
}
