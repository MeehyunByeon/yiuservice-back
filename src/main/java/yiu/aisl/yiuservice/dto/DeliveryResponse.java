package yiu.aisl.yiuservice.dto;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import yiu.aisl.yiuservice.domain.Delivery;
import yiu.aisl.yiuservice.domain.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponse {
    private Long dId;

    private Long studentId;

    private String title;

    private String contents;

    private LocalDateTime due;

    private Byte state;

    private Long food;

    private String link;

    private Long location;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

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
                delivery.getUpdatedAt()
        );
    }
}
