## 🙋‍♂️인사🙋‍♂️

안녕하세요. 세종대학교 그리디 백엔드 4기 이채현입니다.


---
##  Spring MVC 미션 3단계 - 관리자 기능

어드민 페이지 진입은 admin 권한이 있는 사람만 할 수 있도록 제한한다.
만약 진입 권한이 없다면 401 코드를 응답하도록 한다.

---
### 고민한 내용 🤔

- HandlerInterceptor에서 기본적으로 preHandle/postHandle/afterCompletion으로 나뉘어져있던데,
이번의 경우에는 admin페이지 접속만을 확인하면 되는거라서 preHandle만을 사용했습니다.
이경우에는 postHandle과 afterCompletion을 어떻게 구성하면 좋을지..? 알고 싶네요.


- WebConfig 같은 경우에는, admin 페이지를 접속할때의 role만을 확인해주면 되므로 /admin/***을 사용해주었습니다.

- token 만료시간도 있어야할 것 같아서 expire_time을 이용해서,만료시간을 구현해보았습니다. 만료시간을 구현할 더 좋은
방법이 있을지?? 궁금합니다.

- token을 accessToken과 refreshToken으로 나눠서 구현해주었는데, 구현하다보니 너무 어려워서 제대로 구현된건지 잘 
모르겠습니다.



