package roomescape.domain.auth.service;

import org.springframework.stereotype.Service;
import roomescape.domain.auth.principal.LoginMember;
import roomescape.domain.auth.repository.AuthRepository;

@Service
public class AuthService {

    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LoginMember login(String email, String password) {
        return authRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new IllegalArgumentException("아이디 혹은 비밀번호가 잘못되었습니다."));
    }
}
