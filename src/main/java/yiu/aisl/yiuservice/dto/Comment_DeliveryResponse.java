package yiu.aisl.yiuservice.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import yiu.aisl.yiuservice.domain.ActiveEntity;
import yiu.aisl.yiuservice.domain.Comment_Delivery;
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
public class Comment_DeliveryResponse implements ActiveEntity {
    private Long dcId;

    private Long studentId;

    private String nickname;

    private String contents;

    private String details;

    private ApplyState state;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private DeliveryResponse delivery;

    public static Comment_DeliveryResponse GetCommentDeliveryDTO(Comment_Delivery comment_delivery) {
        return new Comment_DeliveryResponse(
                comment_delivery.getDcId(),
                comment_delivery.getUser().getStudentId(),
                comment_delivery.getUser().getNickname(),
                comment_delivery.getContents(),
                comment_delivery.getDetails(),
                comment_delivery.getState(),
                comment_delivery.getCreatedAt(),
                comment_delivery.getUpdatedAt(),
                DeliveryResponse.GetDeliveryDTO(comment_delivery.getDelivery())
        );
    }
}
