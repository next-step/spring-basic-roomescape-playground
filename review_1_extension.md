# 1단계 리뷰 이후에 추가적으로 정리

## 해결 해야 하는 부분!
- JWT는 어떻게 구성되어 있는가? claim에 담긴 값을 볼 수 있는 대상은 누구인가?
- JWT secret key가 외부에 노출 될 경우, 보안적으로 어떤 문제가 있는가?
- JWT를 Member, Reservation, Time과 같은 도메인으로 볼 수 있을까? 곰곰이 생각하는 도메인이란 무엇인가?
- 만약 JWT 방식에서 Session 방식으로 인가/인증 방식이 바뀐다면, Controller와 Service 중 어느 곳이 바뀌어야 할까요?
- Rest Assured 를 이용하여 구현한 API 테스트 작성

## JWT 구성 요소
![JWT 구성요소](https://github.com/user-attachments/assets/abe6ffb8-b3b9-4f13-a2bf-2fe7a69c980c)

> "JSON Web Tokens consist of three parts separated by dots (.)"

- Header
- Payload
- Signature

```text
xxxxx.yyyyy.zzzzz
```

### Header
헤더는 일반적으로 두 부분으로 구성됩니다. `토큰의 타입`, 사용중인 서명 알고리즘 (HMAC SHA256 RSA)
```json
{
  "alg" : "HS256",
  "typ" : "JWT"
}
```
![jwt-result](https://github.com/user-attachments/assets/8d571097-d1ad-4740-93e4-4b895bfe68c5)

### Payload
두 번째 부분으로 payload 는 `클레임` 이라는 것을 포함하고 있습니다. `클레임` 은 엔티티와 추가 데이터에 대한 상태입니다.

> key-value 형식으로 이루어진 한 쌍의 정보를 Claim 이라고 칭한다.

```json
{
  "sub" : "1234567890",
  "name" : "John Doe",
  "admin" : true
}
```
서버와 클라이언트가 주고받는 시스템에서 실제로 사용될 정보에 대한 내용을 담고 있는 부분입니다.
- Registered Claims : 미리 정의된 클레임.
    - iss : 토큰 발급자
    - exp : 만료 시간
    - sub : 제목 - 토큰에서 클라이언트에 대한 식별 값이 됨
    - iat : 발행 시간
    - jti : JWI ID
- Public Claims : 사용자가 정의할 수 있는 클레임 공개용 정보 전달을 위해 사용
- Private Claims : 해당하는 당사자들 간에 정보를 공유하기 위해 만들어진 사용자 지정 클레임.

페이로드는 Base64Url 인코딩 되어 JSON 웹 토큰의 두 번째 부분을 구성합니다.
### Signature
서명은 Header 부분과 Payload 부분을 재사용하여 합치고 서버가 가지고 있는 유일한 key 값을 합친 것을
헤더에서 정의한 알고리즘으로 암호화를 합니다.

```text
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret)
```
서명은 메시지가 도중에 변경되지 않았음을 확인하는 데 사용합니다.
> Header 와 Payload는 단순히 인코딩돤 값이기 때문에 제 3자가 복호화 및 조작할 수 있지만, Signature는 
> 서버 측에서 관리하는 비밀키가 유출되지 않는 이상 복호화할 수 없다. 따라서 Signature는 토큰의 위변조 여부를
> 확인하는데 사용합니다.

클레임은 비밀키를 가지고 있는 서버에서 볼 수 있습니다. 하지만 비밀키가 노출된다면 그 누구라도 복호화하여 인코딩하여 정보를 볼 수 있게 됩니다.


### 출처
- [jwt.io](https://jwt.io/introduction)