package yiu.aisl.yiuservice.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.yiuservice.domain.Delivery;
import yiu.aisl.yiuservice.domain.Taxi;
import yiu.aisl.yiuservice.domain.User;
import yiu.aisl.yiuservice.domain.state.PostState;

import java.util.List;
import java.util.Optional;

@Transactional
public interface TaxiRepository extends JpaRepository<Taxi, Long> {

    Optional<Taxi> findBytId(Long tId);

    List<Taxi> findByStateOrderByCreatedAtDesc(PostState state);

    List<Taxi> findByUser(User user);

    List<Taxi> findByUserAndState(User user, PostState state);

    List<Taxi> findByUserOrderByStateDescCreatedAtDesc(User user);
}
