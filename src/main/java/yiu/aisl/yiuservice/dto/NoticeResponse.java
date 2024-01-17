package yiu.aisl.yiuservice.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import yiu.aisl.yiuservice.domain.Delivery;
import yiu.aisl.yiuservice.domain.Notice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeResponse {
    private Long noticeId;

    private String title;

    private String contents;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static NoticeResponse GetNoticeDTO(Notice notice) {
        return new NoticeResponse(
                notice.getNoticeId(),
                notice.getTitle(),
                notice.getContents(),
                notice.getCreatedAt(),
                notice.getUpdatedAt()
        );
    }

    public static NoticeResponse GetNoticeDetailDTO(Notice notice) {
        return new NoticeResponse(
                notice.getNoticeId(),
                notice.getTitle(),
                notice.getContents(),
                notice.getCreatedAt(),
                notice.getUpdatedAt()
        );
    }
}
