package yiu.aisl.yiuservice.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.yiuservice.domain.Comment_Delivery;
import yiu.aisl.yiuservice.domain.Comment_Taxi;
import yiu.aisl.yiuservice.domain.User;

import java.util.Optional;

@Transactional
public interface Comment_TaxiRepository extends JpaRepository<Comment_Taxi, Long> {
}
