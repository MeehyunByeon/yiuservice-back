package yiu.aisl.yiuservice.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.yiuservice.domain.*;
import yiu.aisl.yiuservice.domain.state.ApplyState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
public interface Comment_DeliveryRepository extends JpaRepository<Comment_Delivery, Long> {

    Optional<Comment_Delivery> findByDcId(Long dcId);

    List<Comment_Delivery> findByDelivery(Delivery delivery);

    List<Comment_Delivery> findByUser(User user);

    List<Comment_Delivery> findByUserAndDelivery(User user, Delivery delivery);

    List<Comment_Delivery> findByDeliveryAndState(Delivery delivery, ApplyState state);

    List<Comment_Delivery> findByUserAndState(User user, ApplyState state);

//    List<Comment_Delivery> findByUserAndStateAndDueAfter(User user, ApplyState state, LocalDateTime currentTime);
}
