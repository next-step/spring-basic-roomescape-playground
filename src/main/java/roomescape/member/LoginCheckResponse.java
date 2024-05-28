package roomescape.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
//final 필드와 @NonNull 필드에 대해 필요한 생성자 자동 생성
//응답 데이터를 담기 위한 DTO클래스
public class LoginCheckResponse {

    final private String name;
    //final 키워드가 붙어있어 반드시 초기화해야한다.
    //접근 제어자는 private로 설정되어 있어 외부에서 직접 접근할 수 없고 @Getter에 의해 생성된 GetName()메서드를 통해 접근 가눙

}
