package yiu.aisl.yiuservice.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import yiu.aisl.yiuservice.domain.*;
import yiu.aisl.yiuservice.domain.state.ApplyState;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment_TaxiResponse implements ActiveEntity {

    private Long tcId;

    private Long studentId;

    private String nickname;

    private String contents;

    private String details;

    private Integer number;

    private ApplyState state;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static Comment_TaxiResponse GetCommentTaxiDTO(Comment_Taxi comment_taxi) {
        return new Comment_TaxiResponse(
                comment_taxi.getTcId(),
                comment_taxi.getUser().getStudentId(),
                comment_taxi.getUser().getNickname(),
                comment_taxi.getContents(),
                comment_taxi.getDetails(),
                comment_taxi.getNumber(),
                comment_taxi.getState(),
                comment_taxi.getCreatedAt(),
                comment_taxi.getUpdatedAt()
        );
    }
}
