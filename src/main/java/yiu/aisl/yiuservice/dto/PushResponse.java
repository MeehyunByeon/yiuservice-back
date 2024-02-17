package yiu.aisl.yiuservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yiu.aisl.yiuservice.domain.Notice;
import yiu.aisl.yiuservice.domain.Push;
import yiu.aisl.yiuservice.domain.state.EntityCode;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushResponse {
    private Long pushId;

    private EntityCode type;

    private Long id;

    private String contents;

    private LocalDateTime createdAt;

    public static PushResponse GetPushDTO(Push push) {
        return new PushResponse(
                push.getPushId(),
                push.getType(),
                push.getId(),
                push.getContents(),
                push.getCreatedAt()
        );
    }
}
