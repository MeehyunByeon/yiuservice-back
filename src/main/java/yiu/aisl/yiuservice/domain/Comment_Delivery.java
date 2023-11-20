package yiu.aisl.yiuservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment_Delivery {
    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long dcId;

    @ManyToOne
    @JoinColumn(name = "d_id", nullable = false)
    private Delivery delivery;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Byte state;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;
}
