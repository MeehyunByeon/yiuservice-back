package yiu.aisl.yiuservice.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.yiuservice.domain.Delivery;
import yiu.aisl.yiuservice.domain.User;

import java.util.Optional;

@Transactional
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Optional<Delivery> findBydId(Long dId);
}
