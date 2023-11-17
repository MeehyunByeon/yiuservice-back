package yiu.aisl.yiuservice.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.yiuservice.domain.User;

import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByStudentId(Long studentId);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByRefreshToken(String refreshToken);
}