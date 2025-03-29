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

## JWT를 Member, Reservation, Time과 같은 도메인으로 볼 수 있을까? 곰곰이 생각하는 도메인이란 무엇인가?
Jwt 를 관리하는 클래스가 도메인 클래스라고 볼 수있는가? 조금 애매하게 알고 있는것 같다. 도메인에 대해서 정리해보고
jwt는 도메인이 아닌 이유를 생각해보자. 아니라면 무엇인지 알아보자.

### 도메인
도메인이란 **비즈니스 로직을 중심으로 애플리케이션이 해결해야할 문제 영역**이다.
> 소프트웨어 공학에서 도메인 모델이란 특정 문제와 관련된 모든 주제의 개념 모델이다. 도메인 모델은 다양한
> 엔티티, 엔티티의 속성, 역할, 관계, 제약을 기술한다. 

단순히 Member, Channel 같은 객체(엔티티)를 의미하는 것이 아니라, 비즈니스 규칙을 포함하여 해당 객체들이
어떻게 **_상호작용하고 데이터를 처리하는지를 포함한 개념_** 이다.

그렇다면 특정 엔티티에 속하지 않는 jwt나 결제 로직이라던지 하는 부분들은 무엇인가?

비즈니스 로직인 "유저가 로그인하면 JWT 토큰을 발급해야 한다." 라는 규칙이 존재하는데, jwt 는 단지 토큰을 생성
, 파싱 해주는 역할을 한다. 이는 도메인 모델에 영향을 주지않는다. 멤버의 데이터에 영향을 주지않는다.
(데이터베이스에 토큰을 저장하지 않는다고 가정.)

JwtProvider 는 그저 데이터를 암호화/복호화하는 역할만 수행한다. 도메인은 "사용자가 로그인할 수 있어야한다" 같은 비즈니스 규칙
이지만, "로그인을 JWT 처리한다" 같은 것은 세부적인 구현의 선택이다. 

기술적인 부분은 비즈니스 로직을 수행하는 것이 아니라, 특정 기능을 가능하게 해주는 기술적인 구현체를 의미한다.
JWT 자체는 비즈니스 로직을 수행하지 않고, 단순히 인증 및 권한 관리를 위한 기술적인 도구일 뿐이다. 결제도 마찬가지이다.
단순히 결제를 위한 기술적인 도구이다.

- 사용자가 로그인하면 JWT를 발급한다. -> 비즈니스 규칙
- JWT는 특정 라이브러리를 이용해 생성하고 검증한다 -> 기술적인 구현

비즈니스 로직이 변경되어 JWT 자체가 필요없어지고 세션 등 다른 방법으로 바뀐다면
- 로그인한다 라는 비즈니스 로직은 그대로지만, JWT 는 필요 없게 됨.
- JWT 자체가 핵심 도메인 모델과 직접적으로 연결되지 않음

JWT는 비즈니스 로직이 아니라 보안 및 인증을 위한 기술적인 기능이므로 서비스 레이어에서 직접 관리하지
않도록 바꿀 수 있을 것 같다. 컨트롤러, 필터, 인터셉터에서 구현을 하는 방법이 있을 것 같다.

### 컨트롤러에서 처리?
```java
@RestController
public class MemberController {
    private final JwtProvider jwtProvider;

  @PostMapping("/login")
  public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
    Member member = memberService.findByEmailAndPassword(request);
    
    String token = jwtProvider.generate(member);

    ResponseCookie cookie = createCookie(loginResponse);

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
  }
}
```
돌아는 갈 것 같은데, 문제점이 뭐지?

### 서비스 레이어에서 토큰을 생성하는 서비스를 이용
```java
@Service
public class AuthService {
    
    private final TokenProvider tokenProvider;
    
    // 로그인 처리를 한다...? 세션과 토큰과의 차이점? 둘 다 뭔가를 하는데 뭔가를 하는지 정확하게 추상화가 되지 않는다.
    public LoginResponse something(Member member) {
        return new LoginResponse(tokenProvider.generateToken(member));
    }
}

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final AuthService authService;
    
    //...
}
```

### JWT 토큰에서 세션으로 바꾸어야할 떄
앞 서 UserService에서 AuthService를 SessionService로 교환하게 되면 수정이 조금은 적어질 것 같다. 그러기 위해서는 
AuthService가 Token 과 Session 방식에서 공통적인 부분을 추상화하여 명세를 잘 뽑아야할 것 같은데, 쉽지는 않은 부분인 것 
같다.

# 해당 부분에 대해서 분리해서 정리합니다.
너무 길어지고 있어서 해당 개념들만을 정리하기 위한 md 파일로 분리하겠습니다.

