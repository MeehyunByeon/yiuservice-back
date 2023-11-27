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
public class Report {
    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long reportId;

    @ManyToOne
    @JoinColumn(name = "from_id")
    private User fromId;

    @ManyToOne
    @JoinColumn(name = "to_id")
    private User toId;

    @Column(columnDefinition = "TEXT")
    private String contents;

    @Column
    private Byte type;

    @Column
    private Long id;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;
}
