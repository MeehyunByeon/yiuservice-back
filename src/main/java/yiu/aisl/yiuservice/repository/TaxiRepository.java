package yiu.aisl.yiuservice.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.yiuservice.domain.Taxi;
import yiu.aisl.yiuservice.domain.User;

import java.util.Optional;

@Transactional
public interface TaxiRepository extends JpaRepository<Taxi, Long> {
}
