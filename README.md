# ⭐️ 1단계

## 🍀기능 구현

### 🔧 로그인 기능 구현

* [ ] Controller, Service, Dto 생성
* [ ] /login에 email, password값을 body에 포함 

### 🔧 Cookie를 이용하여 사용자 정보 조회 API 구현

* [x] email, password로 멤버 조회
* [ ] name으로 토큰 생성
* [ ] Cookie에 "token"값으로 토큰이 포함되도록 설정
* [ ] timeout = 60, secretkey = 임의의 문자열

## 🍀고려한 점

🍖 미리 짜여진 코드 컨벤션과 최대한 따라가기 위해서 final과 같은 키워드, ResponseEntity 타입 명시 등을 사용하지 않음.
🍖 The signing key's size is 72 bits which is not secure enough for the HS256 algorithm. -> 더 긴 secretkey필요

## 🍀궁금한 점
❓ Jwt의 secret키를 모르는 경우 테스트 가능 여부 및 신뢰성 여부 -> JWT.io 사이트를 이용해야 하는 것인지
❓ 현재 학습 자료의 Dao, Dto, Controller, Service가 한 패키지 안에 구조되어 있는데 다른 의도가 존재하는 것인지

# ⭐️ 2단계

## 🍀기능 구현

### 🔧 HandlerMethodArgumentResolver구현
* [ ] Cookie를 통해 멤버 정보를 조회하는 로직 분리
* [ ] 전처리를 통해 객체 주입

### 🔧 Cookie를 이용하여 예약 생성 기능 변경
* [ ] name값 유무에 따른 예약 생성


## 🍀고려한 점
🍖@PathVariable 등을 처리하는 친구가 HandlerMethodArgumentResolver 이 친구인 것을 알게 됨
