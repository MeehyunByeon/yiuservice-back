package yiu.aisl.yiuservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {
    private String accessToken;
    private String refreshToken;
}

//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//public class TokenDto {
//    @Id
//    @JsonIgnore
//    private Long studentId;
//
//    private String refresh_token;
//
//    @TimeToLive(unit = TimeUnit.SECONDS)
//    private Integer expiration;
//
//    public void setExpiration(Integer expiration) {
//        this.expiration = expiration;
//    }
//}
