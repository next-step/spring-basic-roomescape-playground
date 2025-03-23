# JWT
![Image](https://github.com/user-attachments/assets/1ebaec4f-4091-4f6a-b94c-86e5614aeaa7)

로그인 처리를 한 후에 쿠키에 JWT 토큰 값을 리턴받는다.

![Image](https://github.com/user-attachments/assets/b193ed0f-2e8a-472c-b84a-e13dcc3f0b5f)

비밀키가 존재하지 않지만 복호화할 수 있다.

**JWT**
- 특성
  - JWT 는 비밀키를 모르더라도 복호화가 가능하다.
  - JWT 를 가진 사람이라면 누가라도 해당 토큰에 어떤 데이터가 담겨있는지 확인할 수 있습니다.
  - 변조가 불가능할 뿐, 누구나 복호화하여 사용하여 보는 것이 가능하다.

그럼 사용하는 이유가 무엇일까? 
- 로그인 한 유저를 식별하기 위해서 사용한다.
- 서버에 별도로 저장하지 않는 (세션을 사용하지 않는) 무상태로 관리할 수 있다.
- 변조, 즉 클라이언트가 해당 토큰을 변조하여 다른 유저로 변경하거나, 관리자로 변조하는 등 변조에 대해서 식별 가능하다.

즉, 서버에서 발급한 JWT 인지를 식별하고, 변조되었는지를 검사하는 것이 JWT를 사용하여 인증을 하는 이유라고 생각된다.

변조 또는 서버에서 발급한 JWT 토큰인지 아닌지를 검증하기 위해서 secret key 가 존재한다. 그렇기에 클라이언트가 
토큰을 변조하지 못하도록 비밀키가 노출되면 안된다.

그렇다면 토큰에는 어떤 값을 담아주어야 할까?
- 노출되더라도 사용자의 개인정보, 비밀번호를 알 수 없도록 해야한다.

JWT 토큰이 검증하면서 예외가 발생하는 경우

### 참조
- [JWT](https://velog.io/@wjsqjqtk/JWTJson-Web-Token)

## jwt 토큰을 파싱하며 생기는 예외를 핸들링
해당 내용을 정리하면서 중요하다고 생각한점. Secret Key 를 통해서 요청이 서버에서 발급한 토큰에서 변조가 있었는지 즉, 
토큰이 유효한 값인지 검증하고 예외를 핸들링 하는 것이다.

예외를 정확한 이유로 핸들링하기 위해서 parse 하는 메소드의 스펙을 확인했습니다.
```java
  Jws<Claims> parseClaimsJws(String var1) throws ExpiredJwtException, 
                            UnsupportedJwtException, 
                            MalformedJwtException, 
                            SignatureException, 
                            IllegalArgumentException;
```
- `ExpiredJwtException` : 토큰의 유효 기간(exp 클레임)이 지나서 만료된 경우
- `UnSupportedJwtException` : 지원되지 않는 JWT 형식 또는 알고리즘을 사용할 경우
- `MalFormedJwtException` : JWT 문자열 구조가 잘못되어 파싱할 수 없는 경우 (. dot 으로 나뉘는 구조가 아닌경우)
- `SignatureException` : JWT의 서명이 유효하지 않은 경우 (서명 위조 또는 변조)  서버에서 사용하는 key가 맞는지, 토큰이 변조되었는지 확인
- `IllegalArgumentException` : 잘못된 인자가 전달된 경우
