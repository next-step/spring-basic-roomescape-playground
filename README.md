# spring-basic-roomescape-playground

## 🚀 1단계 - 로그인
### 요구사항
- [x] 로그인 기능을 구현하세요.
- [x] 로그인 후 Cookie를 이용하여 사용자의 정보를 조회하는 API를 구현하세요.

### 세부 요구사항
#### 로그인 기능
- [x] 아래의 request와 response 요구사항에 따라
    - [x] `/login`에 email, password 값을 body에 포함하세요.
    - [x] 응답에 Cookie에 "token" 값으로 토큰이 포함되도록 하세요.
#### Request
```http request
POST /login HTTP/1.1
content-type: application/json
host: localhost:8080

{
        "password": "password",
        "email": "admin@email.com"
}
```

#### Response

```
HTTP/1.1 200 OK
Content-Type: application/json
Keep-Alive: timeout=60
Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
```
#### 인증 정보 조회
- [x] 상단바 우측 로그인 상태를 표현해주기 위해 사용자의 정보를 조회하는 API를 구현하세요.
- [x] Cookie를 이용하여 로그인 사용자의 정보를 확인하세요.

#### Request

```http request
GET /login/check HTTP/1.1
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
host: localhost:8080
```

#### Response

```
HTTP/1.1 200 OK
Connection: keep-alive
Content-Type: application/json
Date: Sun, 03 Mar 2024 19:16:56 GMT
Keep-Alive: timeout=60
Transfer-Encoding: chunked

{
    "name": "어드민"
}
```
### 요구사항 테스트
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {
    @Test
    void 일단계() {
        Map<String, String> params = new HashMap<>();
        params.put("email", "admin@email.com");
        params.put("password", "password");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract();

        String token = response.headers().get("Set-Cookie").getValue().split(";")[0].split("=")[1];
        assertThat(token).isNotBlank();

        ExtractableResponse<Response> checkResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .extract();

        assertThat(checkResponse.body().jsonPath().getString("name")).isEqualTo("어드민");
    }
}
```
## 🚀 2단계 - 로그인 리팩터링
### 요구사항
- [x] 사용자의 정보를 조회하는 로직을 리팩터링 합니다.
- [x] 예약 생성 API 및 기능을 리팩터링 합니다.

### 세부 요구사항
#### 로그인 리팩터링
- [x] Cookie에 담긴 인증 정보를 이용해서 멤버 객체를 만드는 로직을 분리합니다.
  - [x] HandlerMethodArgumentResolver을 활용하면 회원정보를 객체를 컨트롤러 메서드에 주입할 수 있습니다.

#### 예약 생성 기능 변경
- [x] 예약 생성 시 ReservationReqeust의 name이 없는 경우 Cookie에 담긴 정보를 활용하도록 리팩터링 합니다.
  - [x] ReservationReqeust에 name값이 있으면 name으로 Member를 찾고
  - [x] 없으면 로그인 정보를 활용해서 Member를 찾도록 수정합니다.

![img.png](img.png)
#### 쿠키를 이용한 예약 생성 Request

```http request
POST /reservations HTTP/1.1
content-type: application/json
cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI
host: localhost:8080

{
    "date": "2024-03-01",
    "theme": "1",
    "time": "1"
}
```

### 요구사항 테스트
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {
    @Test
    void 이단계() {
        String token = createToken("admin@email.com", "password");  // 일단계에서 토큰을 추출하는 로직을 메서드로 따로 만들어서 활용하세요.

        Map<String, String> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("time", "1");
        params.put("theme", "1");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .post("/reservations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.as(ReservationResponse.class).getName()).isEqualTo("어드민");

        params.put("name", "브라운");

        ExtractableResponse<Response> adminResponse = RestAssured.given().log().all()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .post("/reservations")
                .then().log().all()
                .extract();

        assertThat(adminResponse.statusCode()).isEqualTo(201);
        assertThat(adminResponse.as(ReservationResponse.class).getName()).isEqualTo("브라운");
    }
}
```
## 🚀 3단계 - 관리자 기능
### 요구사항
- [x] 어드민 페이지 진입은 `admin` 권한이 있는 사람만 할 수 있도록 제한하세요.
- [x] HandlerInterceptor를 활용하여 권한이 없는 경우 401코드를 응답하세요.

> [🧐 고민 포인트] 어드민만 요청해야 하는 API도 권한 체크를 하려면 어떻게 해야할까요?

### 요구사항 테스트
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {
    @Test
    void 삼단계() {
        String brownToken = createToken("brown@email.com", "password");

        RestAssured.given().log().all()
                .cookie("token", brownToken)
                .get("/admin")
                .then().log().all()
                .statusCode(401);

        String adminToken = createToken("admin@email.com", "password");

        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .get("/admin")
                .then().log().all()
                .statusCode(200);
    }
}
```
