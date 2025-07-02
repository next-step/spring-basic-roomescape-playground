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

### 4. 예약 관리
- **예약 생성**: `POST /reservations`
  - body: `{ date, theme, time }`
  - 성공 시 201 Created + `Location` 헤더 + 예약 정보(JSON) 반환
- **내 예약 조회**: `GET /reservations-mine`
  - 로그인 사용자 본인의 모든 예약 내역을 JSON 배열로 반환

### 5. 대기열 관리
- **대기열 등록**: `POST /waitings`
  - body: `{ date, theme, time }`
  - 성공 시 201 Created + 대기순서(`waitingNumber`) 포함한 JSON 반환
- **대기열 취소**: `DELETE /waitings/{id}`
  - 해당 대기 엔트리 삭제, 204 No Content 반환  

### 6. 서비스 레이어 및 EntityManager 활용
-  `@PersistenceContext`를 통해 주입된 `EntityManager`를 사용






