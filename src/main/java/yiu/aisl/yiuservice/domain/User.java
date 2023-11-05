package yiu.aisl.yiuservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.apache.ibatis.annotations.Update;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity // 테이블과 링크될 클래스(카멜케이스 + _ => 테이블 이름 매칭) // ex) SalesManager.java -> sales_manager table
@Getter
@Setter
@Builder // 1) 해당 클래스 빌더 패턴 클래스 생성 2) 생성자 상단 선언 -> 생성자에 포함된 필드만 빌더에 포함
@AllArgsConstructor
@NoArgsConstructor // 1) 기본 생성자 자동 추가 2) public Posts() {}와 같은 효과
public class User {
    @Id // pk
    private Long studentId;

    @Column(nullable = false, unique = true, columnDefinition = "varchar(10)")
    private String nickname;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    private String pwd;

    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    @Column
    private Byte warn;

    @Column(columnDefinition = "TEXT")
    private String fcm;

    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;

//    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
}
