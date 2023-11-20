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
public class Delivery {
    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long dId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime due;

    @Column(nullable = false)
    @ColumnDefault("1")
    private Byte state;

    @Column
    private Integer food;

    @Column(columnDefinition = "TEXT")
    private String link;

    @Column
    private Integer location;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;
}
