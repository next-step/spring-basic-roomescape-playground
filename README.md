# 1단계 요구사항

- 로그인 기능을 구현하세요.
- 로그인 후 Cookie를 이용하여 사용자의 정보를 조회하는 API를 구현하세요.

### 세부 요구사항

##### 로그인 기능

- 아래의 request와 response 요구사항에 따라
    - /login에 email, password 값을 body에 포함하세요.
    - 응답에 Cookie에 "token"값으로 토큰이 포함되도록 하세요.

Request

```http
POST /login HTTP/1.1
content-type: application/json
host: localhost:8080

{
    "password": "password",
    "email": "admin@email.com"
}
```

Response

```http
HTTP/1.1 200 OK
Content-Type: application/json
Keep-Alive: timeout=60
Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI; Path=/; HttpOnly
```

##### 인증 정보 조회

- 상단바 우측 로그인 상태를 표현해주기 위해 사용자의 정보를 조회하는 API를 구현하세요.
- Cookie를 이용하여 로그인 사용자의 정보확인하세요.

Request

```http
GET /login/check HTTP/1.1
cookie: _ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
host: localhost:8080
```

Response

```http
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

# 2단계 요구사항

- 사용자의 정보를 조회하는 로직을 리팩터링 합니다.
- 예약 생성 API 및 기능을 리팩터링 합니다.

### 세부 요구사항

##### 로그인 리팩터링

- Cookie에 담긴 인증 정보를 이용해서 멤버 객체를 만드는 로직을 분리합니다.
    - HandlerMethodArgumentResolver을 활용하면 회원정보를 객체 를 컨트롤러 메서드에 주입할 수 있습니다.

##### 예약 생성 기능 변경

- 예약 생성 시 ReservationReqeust의 name이 없는 경우 Cookie에 담긴 정보를 활용하도록 리팩터링 합니다.
    - ReservationReqeust에 name값이 있으면 name으로 Member를 찾고
    - 없으면 로그인 정보를 활용해서 Member를 찾도록 수정합니다.

Request

```http
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

# 3단계 요구사항

- 어드민 페이지 진입은 admin권한이 있는 사람만 할 수 있도록 제한하세요.
- HandlerInterceptor를 활용하여 권한이 없는 경우 401코드를 응답하세요.

# 4단계 요구사항

- JPA를 활용하여 데이터베이스에 접근하도록 수정하세요.

### 세부 요구사항

##### gradle 의존성 추가

- build.gradle 파일을 이용하여 다음 의존성을 대체하세요.
    - as is: spring-boot-stater-jdbc
    - to be: spring-boot-starter-data-jpa

##### 엔티티 매핑

- 다른 클래스를 의존하지 않는 클래스 먼저 엔티티 설정을 하세요.
    - ex) Theme나 Time 등

##### 연관관계 매핑

- 다른 클래스에 의존하는 클래스는 연관관계 매핑을 추가로 하세요.
    - ex) Reservation은 Member나 Theme 등의 객체에 의존합니다.

# 5단계 요구사항

- 내 예약 목록을 조회하는 API를 구현하세요.

### 세부 요구사항

##### 내 예약 목록 기능

- 아래의 request와 response 요구사항에 따라 기능을 구현하세요.

Request

```http
GET /reservations-mine HTTP/1.1
cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM
host: localhost:8080
```

Response

```http
HTTP/1.1 200 
Content-Type: application/json

[
    {
        "reservationId": 1,
        "theme": "테마1",
        "date": "2024-03-01",
        "time": "10:00",
        "status": "예약"
    },
    {
        "reservationId": 2,
        "theme": "테마2",
        "date": "2024-03-01",
        "time": "12:00",
        "status": "예약"
    },
    {
        "reservationId": 3,
        "theme": "테마3",
        "date": "2024-03-01",
        "time": "14:00",
        "status": "예약"
    }
]
```

# 6단계 요구사항

- 예약 대기 요청 기능을 구현하세요.
- 예약 대기 취소 기능도 함께 구현하세요.
- 내 예약 목록 조회 시 예약 대기 목록도 함께 포함하세요.
- 중복 예약이 불가능 하도록 구현하세요.
- 심화 요구사항 - 내 예약 목록의 예약 대기 상태에 몇 번째 대기인지도 함께 표시하세요.
