package yiu.aisl.yiuservice.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.yiuservice.domain.Delivery;
import yiu.aisl.yiuservice.domain.Push;
import yiu.aisl.yiuservice.domain.User;

import java.util.List;

@Transactional
public interface PushRepository extends JpaRepository<Push, Long> {
    List<Push> findByUser(User user);
}
