package yiu.aisl.yiuservice.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.yiuservice.domain.Delivery;
import yiu.aisl.yiuservice.domain.User;
import yiu.aisl.yiuservice.domain.state.PostState;

import java.util.List;
import java.util.Optional;

@Transactional
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Optional<Delivery> findBydId(Long dId);

    List<Delivery> findByStateOrderByCreatedAtDesc(PostState state);

    List<Delivery> findByUserAndState(User user, PostState state);
}
