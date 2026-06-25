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
