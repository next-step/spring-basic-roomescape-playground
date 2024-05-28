package roomescape.member;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
//AuthorixationException이 발생할 때 자동으로 Http응답 상태 코드를 401로 설정
//예외가 발생하면 클라이언트에게 401 상태 코드가 반환
public class AuthorizationException extends RuntimeException {
    public AuthorizationException() {
    }
    //기본 생성자 매개변수가 없는 경우 호출

    public AuthorizationException(String message) {
        super(message);
    }
    //메시지를 인자로 받는 생성자
    //super(message)를 호출하여 부모 클래스인 RuntimeException의 생성자를 호출하고 예외 메시지 설정
}
//인증되지 않은 사용자가 접근을 시도할 때 사용
//AdminInterceptor에서 AuthorizationException을 사용하면
//예외 발생시 401상태 코드와 함께 사용자 정의 메시지를 포함하여 응답을 반환 할 수 있음