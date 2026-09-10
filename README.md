# spring-basic-roomescape-playground

---

## User Requirement

- 사용자는 로그인을 할 수 있다.
  - 로그인에 성공하면 상단바 우측의 Login 버튼이 사용자 이름으로 변한다.
  - 로그아웃하면 다시 Login 버튼이 노출된다.

## 1단계 - 로그인

- [x] `POST /login` 요청 시 `email`, `password`로 로그인한다.
  - [x] `email`, `password`로 멤버를 조회한다.
  - [x] 조회한 멤버 정보로 토큰을 생성한다.
  - [x] 응답 Cookie에 `token` 값으로 토큰을 포함한다.
  - [x] `email` 또는 `password`가 틀리면 `401 Unauthorized`로 응답한다.
- [x] `GET /login/check` 요청 시 Cookie를 이용하여 로그인한 사용자의 정보를 조회한다.
  - [x] Cookie에서 `token` 값을 추출한다.
  - [x] 토큰에서 멤버 식별자를 얻어 멤버를 조회하고 이름을 응답한다.
  - [x] Cookie가 없거나 `token` 값이 없으면 `401 Unauthorized`로 응답한다.
