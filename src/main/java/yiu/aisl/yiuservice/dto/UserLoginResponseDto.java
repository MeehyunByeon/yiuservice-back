package yiu.aisl.yiuservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yiu.aisl.yiuservice.domain.User;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponseDto {
    private Long studentId;

    private String nickname;

    private TokenDto token;
}
