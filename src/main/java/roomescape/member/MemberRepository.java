package roomescape.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

	default Member getByEmailAndPassword(String email, String password) {
		return this.findByEmailAndPassword(email, password)
			.orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
	}

	default Member getById(Long id) {
		return this.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid member id"));
	}

	Optional<Member> findByEmailAndPassword(String email, String password);
}
