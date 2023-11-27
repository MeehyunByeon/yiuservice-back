package yiu.aisl.yiuservice.controller;

public enum ResultCode {
    SUCCESS(200, ResultMessage.SUCCESS),
    INSUFFICIENT_DATA(400, ResultMessage.INSUFFICIENT_DATA),
    UNAUTHORIZED(401, ResultMessage.UNAUTHORIZED),
    NO_AUTH(401, ResultMessage.NO_AUTH),
    ACCESS_NO_AUTH(401, ResultMessage.ACCESS_NO_AUTH),
    ACCESS_TOKEN_EXPIRED(403, ResultMessage.ACCESS_TOKEN_EXPIRED),
    REFRESH_TOKEN_EXPIRED(403, ResultMessage.REFRESH_TOKEN_EXPIRED),
    VALID_NOT_STUDENT_ID(401, ResultMessage.VALID_NOT_STUDENT_ID),
    VALID_NOT_PWD(401, ResultMessage.VALID_NOT_PWD),
    MEMBER_NOT_EXIST(401, ResultMessage.MEMBER_NOT_EXIST),
    LOGIN_REQUIRED(401, ResultMessage.LOGIN_REQUIRED),
    NOT_EXIST(401, ResultMessage.NOT_EXIST)
    ;

    private final int resultCode;
    private final String resultMessage;

    ResultCode(int resultCode, String resultMessage) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public interface ResultMessage {
        String SUCCESS = "성공";
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
    }


}
