package yiu.aisl.yiuservice.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.yiuservice.domain.Report;
import yiu.aisl.yiuservice.domain.User;

@Transactional
public interface ReportRepository extends JpaRepository<Report, Long> {
}
