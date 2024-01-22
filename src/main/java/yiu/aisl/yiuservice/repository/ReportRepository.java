package yiu.aisl.yiuservice.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.yiuservice.domain.Report;
import yiu.aisl.yiuservice.domain.User;
import yiu.aisl.yiuservice.domain.state.EntityCode;

import java.util.List;
import java.util.Optional;

@Transactional
public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findAllByOrderByCreatedAtDesc();

    Optional<Report> findByFromIdAndToIdAndTypeAndId(User fromId, User toId, Integer type, Long id);
}
