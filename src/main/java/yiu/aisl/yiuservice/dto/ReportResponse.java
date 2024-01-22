package yiu.aisl.yiuservice.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import yiu.aisl.yiuservice.domain.Delivery;
import yiu.aisl.yiuservice.domain.Report;
import yiu.aisl.yiuservice.domain.User;
import yiu.aisl.yiuservice.domain.state.EntityCode;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {
    private Long reportId;

    private Long fromId;

    private Long toId;

    private String contents;

    private Integer type;

    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static ReportResponse GetReportDTO(Report report) {
        return new ReportResponse(
                report.getReportId(),
                report.getFromId().getStudentId(),
                report.getToId().getStudentId(),
                report.getContents(),
                report.getType(),
                report.getId(),
                report.getCreatedAt(),
                report.getUpdatedAt()
        );
    }
}
