package yiu.aisl.yiuservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {
    @Id // pk
    @Column(unique = true)
    private Long code;

    @Column(nullable = false, length = 50)
    private String location;
}
