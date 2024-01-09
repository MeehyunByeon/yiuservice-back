package yiu.aisl.yiuservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import yiu.aisl.yiuservice.domain.state.PostState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
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

    @Column(nullable = false, length = 50)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;
    
    @Column(nullable = false)
    private LocalDateTime due;

    @Column(nullable = false)
    private PostState state;

    @Column
    @ColumnDefault("0")
    private Integer current;

    @Column(nullable = false)
    private Integer max;

    @Column(length = 100)
    private String start;

    @Column
    private Long startCode;

    @Column(length = 100)
    private String end;

    @Column
    private Long endCode;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "taxi") // 댓글과의 관계 설정
    private List<Comment_Taxi> comments = new ArrayList<>();
}
