package yiu.aisl.yiuservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Push {
    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long pushId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User user;

    @Column
    private Byte type;

    @Column
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String contents;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;
}
