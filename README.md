# RoomEscape 예약 시스템

## 구현 기능

### 1. 로그인 및 쿠키 인증
- `/login` 요청 시 이메일, 비밀번호로 로그인 수행
- 로그인 성공 시 JWT를 생성하고 `token`이라는 이름의 HttpOnly Cookie에 저장

### 2. JWT 기반 사용자 인증 주입
- 모든 로그인 사용자의 인증 정보는 `LoginMemberArgumentResolver`를 통해 컨트롤러 메서드에 `LoginMember` 객체로 자동 주입됨

### 3. 어드민 접근 제어
- `/admin/**` 경로에 접근하려는 경우:
    - `AdminAuthorizationInterceptor`가 JWT를 파싱하여 사용자 역할이 `ADMIN`인지 확인
    - 권한이 없으면 401 Unauthorized 응답 반환

