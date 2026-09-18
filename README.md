# spring-roomescape-playground

## 클래스별 구현 기능 목록

---

## `WebConfig`

### ArgumentResolver 등록 기능

* [x] `WebMvcConfigurer`를 구현해 Spring MVC 설정을 확장한다.
* [x] `LoginMemberArgumentResolver`를 생성자 주입으로 전달받는다.
* [x] `addArgumentResolvers()`를 이용해 `LoginMemberArgumentResolver`를 등록한다.
* [x] 컨트롤러 메서드의 `LoginMember` 파라미터에 로그인 회원 정보를 주입할 수 있도록 구성한다.

### 관리자 권한 인터셉터 등록 기능

* [x] `AdminInterceptor`를 생성자 주입으로 전달받는다.
* [x] `addInterceptors()`를 이용해 `AdminInterceptor`를 등록한다.
* [x] `/admin` 요청에 관리자 권한 검사를 적용한다.
* [x] `/admin/**` 요청에 관리자 권한 검사를 적용한다.

---

## `AdminInterceptor`

### 관리자 권한 검사 기능

* [x] `HandlerInterceptor`를 구현해 컨트롤러 진입 전에 권한을 검사한다.
* [x] 요청의 Cookie에서 `token` 값을 조회한다.
* [x] Cookie에 토큰이 존재하면 `LoginService`를 통해 회원 정보를 조회한다.
* [x] 로그인 회원이 존재하지 않는 경우 `401 Unauthorized`를 응답한다.
* [x] 로그인 회원의 `role`이 `ADMIN`이 아닌 경우 `401 Unauthorized`를 응답한다.
* [x] 관리자 권한을 가진 경우 `true`를 반환해 컨트롤러 요청을 계속 진행한다.
* [x] 유효하지 않은 토큰으로 회원 정보를 조회하지 못한 경우 요청을 차단한다.

### Cookie 토큰 조회 기능

* [x] Cookie 목록에서 이름이 `token`인 Cookie를 찾는다.
* [x] Cookie가 존재하지 않는 경우 빈 문자열을 반환한다.
* [x] `token` Cookie가 존재하는 경우 해당 Cookie의 값을 반환한다.

---

## `LoginController`

### 로그인 기능

* [x] `POST /login` 요청을 처리한다.
* [x] `LoginRequest`를 이용해 이메일과 비밀번호를 전달받는다.
* [x] `LoginService`를 통해 JWT Access Token을 생성한다.
* [x] 생성된 JWT를 `token` Cookie에 저장한다.
* [x] 로그인 Cookie에 `HttpOnly`를 적용한다.
* [x] 로그인 Cookie의 경로를 `/`로 설정한다.
* [x] 로그인 성공 시 `200 OK`를 응답한다.

### 로그인 사용자 조회 기능

* [x] `GET /login/check` 요청을 처리한다.
* [x] `LoginMemberArgumentResolver`가 생성한 `LoginMember`를 컨트롤러 메서드에 주입받는다.
* [x] 로그인한 회원의 이름을 `LoginResponse`에 담아 응답한다.
* [x] 컨트롤러에서 Cookie와 JWT를 직접 해석하는 로직을 제거한다.

### 로그아웃 기능

* [x] `POST /logout` 요청을 처리한다.
* [x] `token` Cookie 값을 빈 문자열로 변경한다.
* [x] Cookie의 `MaxAge`를 `0`으로 설정해 인증 Cookie를 삭제한다.
* [x] 로그아웃 성공 시 `200 OK`를 응답한다.

---

## `LoginMember`

### 로그인 회원 정보 관리 기능

* [x] 로그인한 회원의 식별자를 `Long` 타입의 `id`로 관리한다.
* [x] 로그인한 회원의 이름을 `String` 타입의 `name`으로 관리한다.
* [x] 로그인한 회원의 이메일을 `String` 타입의 `email`로 관리한다.
* [x] 로그인한 회원의 권한을 `String` 타입의 `role`로 관리한다.
* [x] getter 메서드를 통해 로그인 회원 정보를 조회할 수 있도록 구성한다.

---

## `LoginMemberArgumentResolver`

### 로그인 회원 정보 주입 기능

* [x] `HandlerMethodArgumentResolver`를 구현한다.
* [x] 컨트롤러 메서드의 파라미터 타입이 `LoginMember`인 경우 Resolver가 동작하도록 구성한다.
* [x] 컨트롤러 진입 전에 `HttpServletRequest`에서 Cookie 정보를 조회한다.
* [x] Cookie에서 `token` 값을 추출한다.
* [x] JWT의 `subject`에서 회원 식별자를 조회한다.
* [x] JWT 서명 검증에 동일한 Secret Key를 사용한다.
* [x] 추출한 회원 식별자를 이용해 `MemberDao.findById()`로 회원을 조회한다.
* [x] 조회한 `Member` 정보를 이용해 `LoginMember` 객체를 생성한다.
* [x] 생성한 `LoginMember`를 컨트롤러 메서드의 인자로 전달한다.

### Cookie 토큰 조회 기능

* [x] Cookie 목록에서 이름이 `token`인 Cookie를 찾는다.
* [x] Cookie가 없는 경우 빈 문자열을 반환한다.
* [x] `token` Cookie가 존재하는 경우 해당 Cookie의 값을 반환한다.

---

## `LoginRequest`

### 로그인 요청 데이터 관리 기능

* [x] 로그인 이메일을 `String` 타입의 `email`로 전달받는다.
* [x] 로그인 비밀번호를 `String` 타입의 `password`로 전달받는다.
* [x] getter 메서드를 통해 로그인 요청 데이터를 조회할 수 있도록 구성한다.

---

## `LoginResponse`

### 로그인 응답 데이터 관리 기능

* [x] 로그인한 회원의 이름을 `String` 타입의 `name`으로 응답한다.
* [x] 생성자를 통해 응답할 회원 이름을 전달받는다.
* [x] getter 메서드를 통해 회원 이름을 조회할 수 있도록 구성한다.

---

## `LoginService`

### 로그인 인증 기능

* [x] 이메일과 비밀번호를 이용해 `MemberDao.findByEmailAndPassword()`로 회원을 조회한다.
* [x] 조회한 회원의 식별자를 JWT의 `subject`에 저장한다.
* [x] 회원 이름을 JWT의 `name` Claim에 저장한다.
* [x] 회원 권한을 JWT의 `role` Claim에 저장한다.
* [x] Secret Key를 이용해 JWT에 서명한다.
* [x] 생성된 JWT Access Token을 반환한다.

### JWT 회원 조회 기능

* [x] 전달받은 JWT의 서명을 검증한다.
* [x] JWT의 `subject`에서 회원 식별자를 추출한다.
* [x] 회원 식별자를 `Long` 타입으로 변환한다.
* [x] `MemberDao.findById()`를 이용해 로그인 회원을 조회한다.
* [x] 조회한 `Member` 객체를 반환한다.

---

## `MemberDao`

### 로그인 회원 조회 기능

* [x] 이메일과 비밀번호를 조건으로 회원 정보를 조회한다.
* [x] 회원 식별자를 조건으로 회원 정보를 조회하는 `findById()`를 제공한다.
* [x] 회원 이름을 조건으로 회원 정보를 조회하는 `findByName()`을 제공한다.
* [x] 조회한 `id`, `name`, `email`, `role` 값을 이용해 `Member` 객체를 생성한다.

---

## `ReservationController`

### 로그인 정보를 이용한 예약 생성 기능

* [x] `POST /reservations` 요청에서 `ReservationRequest`와 `LoginMember`를 함께 전달받는다.
* [x] 예약 생성 시 `name` 값이 없어도 요청을 허용한다.
* [x] 예약 날짜, 테마 식별자, 시간 식별자가 없는 경우 `400 Bad Request`를 응답한다.
* [x] 예약 생성 처리를 `ReservationService`에 위임한다.
* [x] 예약 생성 성공 시 `201 Created`를 응답한다.
* [x] `Location` 헤더에 생성된 예약의 경로(`/reservations/{id}`)를 담아 응답한다.

---

## `ReservationRequest`

### 예약 생성 요청 데이터 관리 기능

* [x] 예약자 이름을 선택값으로 전달받을 수 있도록 구성한다.
* [x] 예약자 이름이 없는 요청을 허용한다.
* [x] 예약 날짜를 `String` 타입의 `date`로 전달받는다.
* [x] 테마 식별자를 `Long` 타입의 `theme`으로 전달받는다.
* [x] 시간 식별자를 `Long` 타입의 `time`으로 전달받는다.
* [x] 로그인 회원의 이름으로 예약 요청을 다시 구성할 수 있도록 생성자를 제공한다.

---

## `ReservationService`

### 예약자 결정 기능

* [x] `ReservationRequest`에 `name` 값이 존재하는지 확인한다.
* [x] `name` 값이 존재하는 경우 `MemberDao.findByName()`으로 회원을 조회한다.
* [x] `name` 값이 없는 경우 `LoginMember`의 회원 식별자를 이용한다.
* [x] 로그인 회원의 식별자를 이용해 `MemberDao.findById()`로 회원을 조회한다.
* [x] 최종적으로 결정된 회원의 이름을 이용해 예약을 생성한다.

### 예약 생성 기능

* [x] 결정된 회원 이름과 기존 예약 요청 정보를 이용해 새로운 `ReservationRequest`를 생성한다.
* [x] `ReservationDao.save()`를 이용해 예약 정보를 저장한다.
* [x] 생성된 예약 정보를 `ReservationResponse`로 변환한다.
* [x] 관리자 요청에서 `name`이 전달된 경우 해당 회원 이름으로 예약한다.
* [x] 일반 로그인 사용자의 요청에서 `name`이 없는 경우 로그인 회원 이름으로 예약한다.

---

## 인증 및 권한 처리 흐름

### 로그인 사용자 정보 조회

* [x] Cookie의 JWT를 이용해 로그인 회원을 식별한다.
* [x] `LoginMemberArgumentResolver`가 로그인 회원 정보를 생성한다.
* [x] 컨트롤러는 인증 Cookie를 직접 해석하지 않고 `LoginMember`를 전달받아 사용한다.

### 관리자 페이지 접근 제어

* [x] `AdminInterceptor`가 `/admin`, `/admin/**` 요청을 컨트롤러 진입 전에 검사한다.
* [x] 로그인하지 않은 사용자의 관리자 페이지 접근을 차단한다.
* [x] `USER` 권한 사용자의 관리자 페이지 접근을 차단한다.
* [x] `ADMIN` 권한 사용자만 관리자 페이지에 접근할 수 있도록 구성한다.
* [x] 권한이 없는 요청에는 `401 Unauthorized`를 응답한다.
