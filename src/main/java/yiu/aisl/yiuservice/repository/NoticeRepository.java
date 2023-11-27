package yiu.aisl.yiuservice.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.yiuservice.domain.Notice;
import yiu.aisl.yiuservice.domain.User;

@Transactional
public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
