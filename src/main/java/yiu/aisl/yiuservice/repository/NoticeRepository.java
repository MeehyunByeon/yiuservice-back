package yiu.aisl.yiuservice.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.yiuservice.domain.Delivery;
import yiu.aisl.yiuservice.domain.Notice;
import yiu.aisl.yiuservice.domain.User;

import java.util.List;
import java.util.Optional;

@Transactional
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findAllByOrderByCreatedAtDesc();

    Optional<Notice> findByNoticeId(Long noticeId);
}
