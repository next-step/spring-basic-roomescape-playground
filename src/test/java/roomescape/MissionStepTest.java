package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.ReservationResponse;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class MissionStepTest {

    @Test
    void 일단계() {
        Map<String, String> params = new HashMap<>();
        //HashMap 객체 생성, 이 객체를 params라는 변수에 할당, 문자열 키와 값의 쌍을 담는 역할
        params.put("email", "admin@email.com");
        params.put("password", "password");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
        //HTTP요청을 보내는데 사용되는 RestAssurd라이브러리 활용(테스트 작성 간편 도움)
                // '.given().log().all()'부분은 요청을 보내기 전에 요청에 대한 로그를 모두 출력
                //'RestAssured.given()'메소드를 호출하여 요청 시작 이어서 '.log().all()' 호출하여 로그 출력
                .contentType(ContentType.JSON) //요청의 컨텐츠 타입을 json으로 설정
                .body(params) //이 줄은 요청의 본문을 위에서 설정한 params맵으로 설정
                .when().post("/login")// '/login'엔드포인트로 post요청을 보내는 역할
                .then().log().all() //'.then().log().all()' 이후 응답을 검사하기 위해 설정
                .statusCode(200) // 응답의 상태 코드가 200인지 확인
                .extract();//응답을 추출하여 다음 단계에서 사용할 수 있도록 한다

        String token = response.headers().get("Set-Cookie").getValue().split(";")[0].split("=")[1];
        //'response.headers()'는 HTTP 응답에서 모든 헤더를 가져온다
        //'.get("Set-Cookie")'는  "Set-Cookie" 헤더의 값을 가져온다
        //'getValue()'해당 헤더의 값 가져온다
        //.split(";") 헤더 값을 세미콜론;을 기준으로 나눈다 쿠키는 보통 여러 개가 한줄에 쉼표로 구분되어 나타남
        //'[0]' 나눈 결과 중 첫 번째 쿠키를 선택한다
        //.split("=")는 선택한 쿠키를 등호(=)를 기준으로 나눈다. 쿠키의 이름과 값은 등호로 구분된다.
        //[1]은 등호로 나눈 결과 중 두 번째 요소를 선택하여 쿠키의 값을 가져온다
        //마지막으로 이 값을 String token변수에 할당

        // 이 코드 줄은 HTTP응답에서 특정 쿠키의 값을 추출하여 그 값을 토큰 변수에 저장 이 토큰은 후속 요청에서 인증의 용도로 사용 가능

        assertThat(token).isNotBlank();
        //토큰이 비어있지 않은지 확인, 비어있다면 테스트 실패
    }


    public String createToken( String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put(email, "admin@email.com");
        params.put(password, "password");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract();
        String token = response.headers().get("Set-Cookie").getValue().split(";")[0].split("=")[1];
        System.out.println("Generated Token: " + token);
        return token;
    }

    @Test
    void 중간단계(){

        String token = createToken("admin@email.com", "password");

        Map<String, String> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("time", "1");
        params.put("theme", "1");

// 예약 생성 API 호출
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                // 쿠키 설정 부분 수정: 'token' 대신 'Authorization' 헤더에 토큰 설정
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .post("/reservations")
                .then().log().all()
                .extract();
    }

    @Test
    void 이단계() {
        //토큰 생성
        String token = createToken("admin@email.com", "password");

        //예약 생성에 필요한 파라미터 설정
        Map<String, String> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("time", "1");
        params.put("theme", "1");

        //예약 생성 API호출
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
//                .cookie("token", token)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .post("/reservations")
                .then().log().all()
                .extract();

        //API 응답 검증
        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.as(ReservationResponse.class).getName()).isEqualTo("어드민");

        //이름을 지정하여 예약 생성
//        params.put("name", "브라운");
//
//        ExtractableResponse<Response> adminResponse = RestAssured.given().log().all()
//                .body(params)
//                .cookie("token", token)
//                .contentType(ContentType.JSON)
//                .post("/reservations")
//                .then().log().all()
//                .extract();
//
//        // API응답 검증
//        assertThat(adminResponse.statusCode()).isEqualTo(201);
//        assertThat(adminResponse.as(ReservationResponse.class).getName()).isEqualTo("브라운");
    }






}