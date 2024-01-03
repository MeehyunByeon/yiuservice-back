package yiu.aisl.yiuservice.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ErrorCode {
    // 잘못된 요청
    INSUFFICIENT_DATA(400, ResultMessage.INSUFFICIENT_DATA),

    UNAUTHORIZED(401, ResultMessage.UNAUTHORIZED),
    NO_AUTH(401, ResultMessage.NO_AUTH),

    // 권한 없음
    ACCESS_NO_AUTH(403, ResultMessage.ACCESS_NO_AUTH),

    ACCESS_TOKEN_EXPIRED(403,ResultMessage.ACCESS_TOKEN_EXPIRED),
    REFRESH_TOKEN_EXPIRED(403, ResultMessage.REFRESH_TOKEN_EXPIRED),

    VALID_NOT_STUDENT_ID(401, ResultMessage.VALID_NOT_STUDENT_ID),
    VALID_NOT_PWD(401, ResultMessage.VALID_NOT_PWD),
    MEMBER_NOT_EXIST(401, ResultMessage.MEMBER_NOT_EXIST),
    LOGIN_REQUIRED(401, ResultMessage.LOGIN_REQUIRED),

    // 데이터를 찾을 수 없음
    NOT_EXIST(404, ResultMessage.NOT_EXIST),

    // 데이터 충돌
    CONFLICT(409, ResultMessage.CONFLICT),
    // 데이터 중복
    DUPLICATE(409, ResultMessage.DUPLICATE),

    // 서버 오류
    INTERNAL_SERVER_ERROR(500, ResultMessage.INTERNAL_SERVER_ERROR)
    ;


    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public interface ResultMessage {
        String UNAUTHORIZED = "인증 실패";
        String NO_AUTH = "접근 권한 없음";
        String ACCESS_NO_AUTH = "접근 권한 없음";
        String ACCESS_TOKEN_EXPIRED = "AccessToken 만료";
        String REFRESH_TOKEN_EXPIRED = "RefreshToken 만료";
        String VALID_NOT_STUDENT_ID = "가입하지 않은 학번";
        String VALID_NOT_PWD = "잘못된 비밀번호";
        String MEMBER_NOT_EXIST = "존재하지 않는 사용자";
        String LOGIN_REQUIRED = "로그인 필요";
        String INSUFFICIENT_DATA = "데이터 부족";
        String NOT_EXIST = "존재하지 않음";
        String CONFLICT = "데이터 충돌";
        String DUPLICATE = "데이터 중복";
        String INTERNAL_SERVER_ERROR = "내부 서버 오류";
    }


}
