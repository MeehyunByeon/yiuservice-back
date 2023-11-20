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
public class Taxi {
    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long tId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true, length = 50)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;

    @CreationTimestamp
    @Column
    private LocalDateTime due;

    @Column
    @ColumnDefault("1")
    private Byte state;

    @Column
    @ColumnDefault("1")
    private Integer current;

    @Column
    private Integer max;

    @Column(length = 100)
    private String start;

    @Column
    private Integer startCode;

    @Column(length = 100)
    private String end;

    @Column
    private Integer endCode;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;
}
