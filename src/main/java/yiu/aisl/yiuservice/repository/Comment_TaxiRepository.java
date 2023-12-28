package yiu.aisl.yiuservice.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.yiuservice.domain.*;

import java.util.Optional;

@Transactional
public interface Comment_TaxiRepository extends JpaRepository<Comment_Taxi, Long> {

    Optional<Comment_Taxi> findByTcId(Long tcId);

    Optional<Comment_Taxi> findByUserAndTaxi(User user, Taxi taxi);
}
