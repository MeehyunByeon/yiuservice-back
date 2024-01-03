package yiu.aisl.yiuservice.dto;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import yiu.aisl.yiuservice.domain.Delivery;
import yiu.aisl.yiuservice.domain.User;
import yiu.aisl.yiuservice.domain.state.ApplyState;
import yiu.aisl.yiuservice.domain.state.PostState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponse {
    private Long dId;

    private Long studentId;

    private String title;

    private String contents;

    private LocalDateTime due;

    private PostState state;

    private Long food;

    private String link;

    private Long location;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<CommentDto> comment;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentDto {
        private Long dcId;

        private Long studentId;

        private String nickname;

        private String contents;

        private String details;

        private ApplyState state;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;
    }

    public static DeliveryResponse GetDeliveryDTO(Delivery delivery) {
        return new DeliveryResponse(
                delivery.getDId(),
                delivery.getUser().getStudentId(),
                delivery.getTitle(),
                delivery.getContents(),
                delivery.getDue(),
                delivery.getState(),
                delivery.getFood(),
                delivery.getLink(),
                delivery.getLocation(),
                delivery.getCreatedAt(),
                delivery.getUpdatedAt(),
                null // 일단 null로 표시
        );
    }

    public static DeliveryResponse GetDeliveryDetailDTO(Delivery delivery) {
        return new DeliveryResponse(
                delivery.getDId(),
                delivery.getUser().getStudentId(),
                delivery.getTitle(),
                delivery.getContents(),
                delivery.getDue(),
                delivery.getState(),
                delivery.getFood(),
                delivery.getLink(),
                delivery.getLocation(),
                delivery.getCreatedAt(),
                delivery.getUpdatedAt(),
                delivery.getComments().stream()
                        .map(comment -> new CommentDto(
                                comment.getDcId(),
                                comment.getUser().getStudentId(),
                                comment.getUser().getNickname(),
                                comment.getContents(),
                                comment.getDetails(),
                                comment.getState(),
                                comment.getCreatedAt(),
                                comment.getUpdatedAt()
                                ))
                        .collect(Collectors.toList())
        );
    }
}
