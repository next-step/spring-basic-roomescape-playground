# 인증 단계 정리

## 1단계 - 로그인 기능 구현

- 사용자는 `/login` 로 email과 password를 전송하여 로그인
- 로그인 성공 시 JWT 토큰을 생성해 쿠키(`token`)에 저장하여 응답
- 토큰에는 사용자 식별 정보(id, name, role)를 포함
- JWT 토큰은 `JwtProvider`에서 생성

## 2단계 - 인증 정보 활용

- 쿠키에서 토큰을 추출하고 사용자 정보를 조회하는 `AuthService` 구현
- 컨트롤러 메서드에서 `LoginMember`를 바로 사용할 수 있도록 `LoginMemberArgumentResolver` 구현
- 예: 예약 요청 시, name이 없으면 로그인한 사용자의 이름으로 예약 처리
- 인증 정보가 필요한 로직을 깔끔하게 분리하여 재사용성 및 테스트 용이성 향상

## 3단계 - 관리자 권한 처리

- 관리자만 접근 가능한 경로(`/admin)를 `AdminAuthorizationInterceptor`로 보호
- 토큰 기반으로 사용자의 role 정보를 조회하고, ADMIN이 아닐 경우 401(Unauthorized) 응답
- 인터셉터는 `WebConfig`에서 등록
