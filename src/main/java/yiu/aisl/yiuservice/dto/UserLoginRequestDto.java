package yiu.aisl.yiuservice.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequestDto {
    private Long studentId;

    private String pwd;

    private String fcm;
}
