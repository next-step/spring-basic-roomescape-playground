## 1단계 - 로그인

### 흐름
```
POST /login
email, password 받음
→ member 테이블에서 어드민 조회
→ JWT token 생성
→ Set-Cookie: token=... 으로 응답
```
```
GET /login/check
Cookie에서 token 꺼냄
→ JWT 파싱해서 subject의 memberId 확인
→ memberId로 멤버 조회
→ { "name": "어드민" } 응답
```

## 2단계 - 리팩터링

### 요구사항
Cookie에서 로그인 멤버를 꺼내는 코드를 ArgumentResolver로 분리하고,

예약 생성 시 name이 있으면 name을 우선 사용하고,

name이 없으면 로그인 멤버를 사용한다.

### 흐름
```
Cookie token
→ LoginMemberArgumentResolver
→ LoginMember 생성
→ ReservationController에 자동 주입
→ ReservationService에서 예약자 결정
```

#### 예약자 결정 규칙
```
request.name 있음 → request.name으로 Member 찾기
request.name 없음 → LoginMember로 Member 찾기
```
