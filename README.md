# 방탈출 어드민 애플리케이션 미션 

## 기능 목록

### `로그인 기능`

- [x] 로그인 요청을 처리할 `LoginController`를 생성한다.
- [x] 로그인 요청 값을 받기 위한 `LoginRequest`를 생성한다.
- [x] email과 password로 회원 정보를 조회한다.
- [x] 조회한 회원 정보로 JWT 토큰을 생성한다.
- [x] 생성한 토큰을 `token` 이름의 Cookie에 저장한다.
- [x] 로그인 응답에 Cookie를 포함한다.

### `인증 정보 조회 기능`

- [x] `GET /login/check` API를 구현한다.
- [x] 요청의 Cookie 목록에서 `token` 값을 추출한다.
- [x] JWT 토큰에서 회원 식별자를 조회한다.
- [x] 회원 식별자로 회원 정보를 조회한다.
- [x] 로그인 사용자 이름을 응답한다.

### `회원 조회 기능`

- [x] email과 password로 회원을 조회하는 기능을 사용한다.
- [x] 회원 식별자로 회원을 조회하는 기능을 추가한다.

### `로그인 리팩터링`

- [x] 로그인 사용자 정보를 담는 `LoginMember`를 생성한다.
- [x] Cookie를 이용한 로그인 사용자 조회를 `LoginMemberArgumentResolver`로 분리한다.
- [x] `LoginMemberArgumentResolver`에서 로그인 회원 정보를 조회한다.0
- [x] 컨트롤러 메서드에서 `LoginMember`를 주입받도록 수정한다.


### `예약 생성 기능 변경`

- [x] `ReservationController`의 예약 생성 메서드에서 `LoginMember`를 주입받도록 수정한다.
- [x] 예약 요청의 `name`이 있는 경우 `MemberService`를 통해 이름으로 회원을 조회한다.
- [x] 예약 요청의 `name`이 없는 경우 주입받은 `LoginMember`으로 회원을 조회한다.
- [x] 예약 생성에 사용할 `Member`를 `ReservationService`에 전달하도록 수정한다.
- [x] `ReservationService.save()`가 `ReservationRequest`와 `Member`를 함께 전달받도록 수정한다.
- [x] `ReservationService`에서 전달받은 `Member`를 `ReservationDao`에 전달하도록 수정한다.
