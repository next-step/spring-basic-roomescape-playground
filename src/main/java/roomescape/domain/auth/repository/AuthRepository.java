package roomescape.domain.auth.repository;

import roomescape.domain.auth.principal.LoginMember;

import java.util.Optional;

public interface AuthRepository {

    Optional<LoginMember> findByEmailAndPassword(String email, String password);
}
