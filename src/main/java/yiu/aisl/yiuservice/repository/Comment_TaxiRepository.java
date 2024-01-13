package yiu.aisl.yiuservice.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.yiuservice.domain.*;
import yiu.aisl.yiuservice.domain.state.ApplyState;

import java.util.List;
import java.util.Optional;

@Transactional
public interface Comment_TaxiRepository extends JpaRepository<Comment_Taxi, Long> {

    Optional<Comment_Taxi> findByTcId(Long tcId);

    List<Comment_Taxi> findByTaxi(Taxi taxi);

    List<Comment_Taxi> findByUserAndTaxi(User user, Taxi taxi);

    List<Comment_Taxi> findByTaxiAndState(Taxi taxi, ApplyState state);
}
