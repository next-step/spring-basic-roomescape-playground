## 1 단계
- [x] jwt 토큰을 통한 로그인 api 구현
- [x] 인증 정보 조회 api 구현

### 고려한 점:
1. auth와 member 패키지 분리: 복잡도를 줄이기 위함


## 2단계 
- [x] HandlerMethodArgumentResolver를 사용하여 로그인 사용자는 자신의 로그인 정보로 예약을 생성

### 고려한점:
token에 대한 예외 상황에는 무엇이 있을까?
token관련 생길 수 있는 예외
- Token이 필요한 api에서 token이 없는 경우: BadRequest
- Token이 만료된 경우: InvalidCredentialsException
- 로그인 정보가 틀렷을 경우: InvalidCredentialsException
- 유효하지 않은 토큰의 경우 (잘못된 형식): InvalidTokenFormatException

## 3단계: 어드민 페이지 진입은 admin권한이 있는 사람만 할 수 있도록 제한

### 고려한점:
3. Interceptor vs Filter<br/>
Filter: 공통된 보안 및 인증/인가 관련 작업 <br/>
Interceptor: 세부적인 보안 및 인증/인가 공통 작업<br/>
인가와 관련된 스프링 기술(어노테이션으로 API 구분, 핸들러 정보 접근, 스프링 예외 처리 기능)을 활용하기 위해 인가를 Interceptor에서 처리<br/>
Filter로는 전역 인증 처리

API중에서는 토큰을 활용하는 API와 하지 않는 API가 구분 될 거라 생각된다.<br/>
Ex. <br/>
회원가입, 아이디 찾기, 비밀번호 찾기 등은 토큰을 활용하지 않을 것으로 생각이 되는데
인증을 Filter로 처리할때 토큰을 활용하는 API와 활용하지 않는 API를 분리하는 것이 복잡한지 궁금합니다.

