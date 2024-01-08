package yiu.aisl.yiuservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import yiu.aisl.yiuservice.domain.User;

@Getter
@Setter
public class UserJoinRequestDto {

    private Long studentId;

    private String nickname;

    private String pwd;
}