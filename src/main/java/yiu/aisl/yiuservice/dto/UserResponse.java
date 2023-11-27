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
public class UserResponse {
    private Long studentId;

    private String nickname;

    private String pwd;

    private String refreshToken;

    public UserResponse(User user) {
        this.studentId = user.getStudentId();
        this.nickname = user.getNickname();
    }


}