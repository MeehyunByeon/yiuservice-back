package yiu.aisl.yiuservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePwdRequestDTO {
    private Long studentId;

    private String pwd;
}
