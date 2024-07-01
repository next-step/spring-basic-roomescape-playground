## Spring MVC 요구사항 분석
### 1단계 - 로그인
- 로그인 기능을 구현하세요.
- 로그인 후 Cookie를 이용하여 사용자의 정보를 조회하는 API를 구현하세요.
#### 로그인 기능
- 아래의 request와 response 요구사항에 따라 /login에 email, password 값을 body에 포함하세요.
- 응답에 Cookie에 "token"값으로 토큰이 포함되도록 하세요.
#### 인증 정보 조회
- 상단바 우측 로그인 상태를 표현해주기 위해 사용자의 정보를 조회하는 API를 구현하세요.
- Cookie를 이용하여 로그인 사용자의 정보확인하세요.

### 2단계 - 로그인 리팩터링
- 사용자의 정보를 조회하는 로직을 리팩터링 합니다.
- 예약 생성 API 및 기능을 리팩터링 합니다. 
#### 로그인 리팩터링
- Cookie에 담긴 인증 정보를 이용해서 멤버 객체를 만드는 로직을 분리합니다.
  - HandlerMethodArgumentResolver을 활용하면 회원정보를 객체를 컨트롤러 메서드에 주입할 수 있습니다.
#### 예약 생성 기능 변경
- 예약 생성 시 ReservationReqeust의 name이 없는 경우 Cookie에 담긴 정보를 활용하도록 리팩터링 합니다.
  - ReservationReqeust에 name값이 있으면 name으로 Member를 찾고
  - 없으며 로그인 정보를 활용해서 Member를 찾도록 수정합니다.

### 3단계 - 관리자 기능
- 어드민 페이지 진입은 admin권한이 있는 사람만 할 수 있도록 제한하세요.
- HandlerInterceptor를 활용하여 권한이 없는 경우 401코드를 응답하세요.

## Spring JPA 요구사항 분석
### 4단계 - JPA 전환 
- JPA를 활용하여 데이터베이스에 접근하도록 수정하세요.

#### gradle 의존성 추가
- build.gradle 파일을 이용하여 다음 의존성을 대체하세요.
  - as is: `spring-boot-stater-jdbc` to be: `spring-boot-starter-data-jpa`

#### 엔티티 매핑
- 다른 클래스를 의존하지 않는 클래스 먼저 엔티티 설정을 하세요.
  - ex) Theme나 Time 등

#### 연관관계 매핑
- 다른 클래스에 의존하는 클래스는 연관관계 매핑을 추가로 하세요.
  - ex) Reservation은 Member나 Theme 등의 객체에 의존합니다.
