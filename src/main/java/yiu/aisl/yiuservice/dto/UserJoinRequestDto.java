package yiu.aisl.yiuservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import yiu.aisl.yiuservice.domain.User;

//@Data
//@Builder
//@AllArgsConstructor
@Getter
@Setter
public class UserJoinRequestDto {

    private Long studentId;

    private String nickname;

    private String pwd;

//    @NotBlank(message = "학번을 입력해주세요.")
//    @Size(min = 1, max = 9, message = "학번 9자리를 입력해주세요.")
//    private Long studentId;
//
//    @NotBlank(message = "닉네임을 입력해주세요.")
//    @Size(min = 1, max = 10, message = "1~10자 닉네임을 입력해주세요.")
//    private String nickname;
//
//    @NotBlank(message = "비밀번호를 입력해주세요.")
//    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,30}$",
//            message = "알파벳, 숫자, 특수문자를 포함하는 8~30 자리 비밀번호를 입력해주세요.")
//    private String pwd;
//
////    @NotBlank(message = "인증번호를 입력해주세요.")
////    @Size(min=6, max=6)
////    private Integer authNum;
//
//    @Builder
//    public User toEntity() {
//        return User.builder()
//                .studentId(studentId)
//                .nickname(nickname)
//                .pwd(pwd)
//                .build();
//    }
}