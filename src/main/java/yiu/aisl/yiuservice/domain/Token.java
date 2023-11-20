package yiu.aisl.yiuservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@RedisHash("refreshToken")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Id
    @JsonIgnore
    private Long studentId;

    private String refreshToken;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long expiration;

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }
}
