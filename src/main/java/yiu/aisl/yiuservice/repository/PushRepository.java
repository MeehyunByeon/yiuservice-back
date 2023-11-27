package yiu.aisl.yiuservice.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.yiuservice.domain.Push;
import yiu.aisl.yiuservice.domain.User;

@Transactional
public interface PushRepository extends JpaRepository<Push, Long> {
}
