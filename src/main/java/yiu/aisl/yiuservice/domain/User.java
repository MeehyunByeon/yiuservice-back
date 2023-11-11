package yiu.aisl.yiuservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.apache.ibatis.annotations.Update;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity // 테이블과 링크될 클래스(카멜케이스 + _ => 테이블 이름 매칭) // ex) SalesManager.java -> sales_manager table
@Getter
@Builder // 1) 해당 클래스 빌더 패턴 클래스 생성 2) 생성자 상단 선언 -> 생성자에 포함된 필드만 빌더에 포함
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 1) 기본 생성자 자동 추가 2) public Posts() {}와 같은 효과
public class User {
    @Id // pk
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long studentId;

    @Column(nullable = false, unique = true, length = 10)
    private String nickname;

    @Column(nullable = false, length = 255)
    private String pwd;

    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    @Column
    private Byte warn;

    @Column(columnDefinition = "TEXT")
    private String fcm;

//    @CreatedDate
    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

//    @LastModifiedDate
    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;

    //    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)

    public User(Long studentId, String nickname, String pwd, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.studentId = studentId;
        this.nickname = nickname;
        this.pwd = pwd;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void encodePwd(PasswordEncoder passwordEncoder){
        this.pwd = passwordEncoder.encode(pwd);
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public User(Long studentId, String refreshToken) {
        this.studentId = studentId;
        this.refreshToken = refreshToken;
    }

    public User update(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
        return this;
    }
}
