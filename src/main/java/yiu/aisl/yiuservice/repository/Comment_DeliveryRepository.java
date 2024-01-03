package yiu.aisl.yiuservice.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.yiuservice.domain.Comment_Delivery;
import yiu.aisl.yiuservice.domain.Delivery;
import yiu.aisl.yiuservice.domain.User;

import java.util.List;
import java.util.Optional;

@Transactional
public interface Comment_DeliveryRepository extends JpaRepository<Comment_Delivery, Long> {

    Optional<Comment_Delivery> findByDcId(Long dcId);

    List<Comment_Delivery> findByDelivery(Delivery delivery);

    List<Comment_Delivery> findByUserAndDelivery(User user, Delivery delivery);
}
